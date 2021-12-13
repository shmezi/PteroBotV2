package me.alexirving.bot.command.commands

import com.mattmalec.pterodactyl4j.PteroBuilder
import me.alexirving.bot.PteroBot
import me.alexirving.bot.command.Argument
import me.alexirving.bot.command.Command
import me.alexirving.bot.utils.addRow
import me.alexirving.bot.utils.setValue
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType

class Link(val bot: PteroBot) : Command(
    "link",
    "Link your account to pterodactyl panel account!",
    Permission.MESSAGE_SEND,
    Argument(OptionType.STRING, "key", "Your api key!", true)
) {
    override fun execute(event: SlashCommandEvent) {
        event.deferReply().setEphemeral(true).queue()
        val userId = event.user.id

        if (bot.configs[event.guild]!!.getValue("url") == "default") {
            event.hook.editOriginal("An error occurred, this server was not setup correctly!").queue()
            return
        }

        //Adding user to the database if they aren't already in it.
        addRow("${bot.clientId}_keys", userId)

        //Not using session manager since we don't want it to set if it failed.
        try {
            val tempClient =
                PteroBuilder.create(bot.configs[event.guild]!!.getValue("url"), event.getOption("key")!!.asString)
                    .setHttpClient(bot.okHttpClient).buildClient()
            tempClient.retrieveAccount()
                .executeAsync({
                    event.hook.editOriginal("Linked well!").queue()
                    setValue("${bot.clientId}_keys", userId, event.guild!!.id, event.getOption("key")!!.asString)
                    bot.sessionManager.setClient(event.member!!, tempClient)
                }, {
                    event.hook.editOriginal("API key was invalid!").queue()
                })
        } catch (ignored: Exception) {
            event.hook.editOriginal("An error occurred, this server was not setup correctly!").queue()
        }


    }

}