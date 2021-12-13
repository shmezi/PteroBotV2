package me.alexirving.bot.command.commands.server

import me.alexirving.bot.command.Command
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class Server(val sessionManager: SessionManager) :
    Command("server", "Get your epic server!", Permission.MESSAGE_SEND) {
    override fun execute(event: SlashCommandEvent) {
        event.deferReply().setEphemeral(true).queue()
        sessionManager.newSession(event.member!!).fullyLoad {
            event.hook.editOriginalEmbeds(it.getSessionPanel().build()).setActionRow(it.getSessionSelector().components)
                .queue()
        }
    }
}