package me.alexirving.pterobot.struct

import me.alexirving.pterobot.PteroLauncher.users
import me.alexirving.pterobot.pq
import me.alexirving.pterobot.validateAdmin
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.Modal
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle

class InteractionListeners(val bot: Bot) : ListenerAdapter() {
    override fun onSelectMenuInteraction(e: SelectMenuInteractionEvent) {
        if (e.componentId == e.user.id)
            e.deferEdit().queue()
        else
            if (!validateAdmin(bot, e)) {
                e.reply("You can't use someone else's server menu!").queue()
                return
            }

        users.get(e.user.id) { user ->
            user.getCachedSession(bot, e.guild?.id ?: return@get) {
                it?.select(e.selectedOptions[0].value)

            }
        }

    }

    private val cmdInput = TextInput.create("command", "Send a command! (Without a slash)", TextInputStyle.SHORT)
        .setPlaceholder("op notch").setRequired(true).build()

    private val validEmail = "^.+@.+\\..+".toPattern()
    private val emailInput = TextInput.create("email", "Your panel email", TextInputStyle.SHORT)
        .setPlaceholder("alex@irving.net").setRequired(true).build()

    override fun onButtonInteraction(e: ButtonInteractionEvent) {

        users.get(e.user.id) { user ->
            user.getCachedSession(bot, e.guild?.id ?: return@get) {
                when (e.button.id) {
                    "${e.user.id}-start" -> {
                        e.deferEdit().queue()
                        it?.start()

                    }

                    "${e.user.id}-stop" -> {
                        e.deferEdit().queue()
                        it?.stop()
                    }

                    "${e.user.id}-restart" -> {
                        e.deferEdit().queue()
                        it?.reStart()
                    }

                    "${e.user.id}-cmd" -> {
                        val cmdModal = Modal.create("${e.user.id}-cmd", "Remote console")
                            .addActionRows(ActionRow.of(cmdInput))
                            .build()

                        e.replyModal(cmdModal).queue()
                    }

                    "${e.user.id}-access" -> {
                        if (validateAdmin(bot, e, false)) {
                            val m = Modal.create("${e.user.id}-access", "Access servers for staff!")
                                .addActionRows(ActionRow.of(emailInput)).build()
                            e.replyModal(m).queue()
                        } else
                            e.reply("NO PERMISSION!").queue()
                    }

                    else -> e.button.id.pq("ERROR button id found with:")
                }
            }
        }
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        val member = event.modalId.split("-")[0]
        val type = event.modalId.split("-")[1]

        when (type) {
            "cmd" -> {
                users.get(member) { user ->
                    user.getCachedSession(bot, event.guild?.id ?: return@get) {
                        it?.sendCommand(event.getValue("command")?.asString ?: return@getCachedSession)
                    }
                }
                event.reply("Command sent!").queue()
            }

            "access" -> {
                val email = event.getValue("email")?.asString ?: return
                if (validEmail.matcher(email).matches())
                    users.get(member) { user ->
                        user.getCachedSession(bot, event.guild?.id ?: return@get) {
                            it?.access(email)
                            event.reply("You have been granted access to the server!").setEphemeral(true).queue()
                        }
                    }
                else
                    event.reply("Invalid email provided!").setEphemeral(true).queue()
            }
        }

    }
}