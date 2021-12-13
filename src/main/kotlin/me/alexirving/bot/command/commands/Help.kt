package me.alexirving.bot.command.commands

import me.alexirving.bot.command.Command
import me.alexirving.bot.command.CommandHandler
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class Help(val commandHandler: CommandHandler) :
    Command("help", "List all of the commands or get help with a specific one.", Permission.MESSAGE_HISTORY) {
    override fun execute(event: SlashCommandEvent) {
        val builder = EmbedBuilder()
        builder.setTitle("Help menu:")
        for (command: Command in commandHandler.commandMap.values) {
            builder.appendDescription("\n**${command.name}** - `${command.description}`")
        }
        event.replyEmbeds(builder.build()).setEphemeral(true).queue()
    }
}