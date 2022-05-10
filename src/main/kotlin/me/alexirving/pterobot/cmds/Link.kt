package me.alexirving.pterobot.cmds

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.Suggestion
import dev.triumphteam.cmd.slash.sender.SlashSender
import me.alexirving.pterobot.PteroBot
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

@Command("link")
class Link(val bot: PteroBot) : BaseCommand() {


    @Default
    fun link(e: SlashSender, apikey: String) {
        val eg = e.guild
        if (eg == null) {
            e.reply("Commands must be run inside a guild!").setEphemeral(true).queue()
            return
        }
        e.deferReply(true).queue()
        val b = bot.client(apikey)
        if (b != null)
            bot.users.get(apikey, true) {
                it.guilds[eg.id] = apikey
            }
        else
            e.hook.editOriginal("Sorry api key is invalid!").queue()


    }
}