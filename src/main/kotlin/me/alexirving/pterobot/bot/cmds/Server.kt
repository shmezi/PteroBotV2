package me.alexirving.pterobot.bot.cmds

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.Optional
import dev.triumphteam.cmd.slash.sender.SlashSender
import me.alexirving.pterobot.PteroLauncher.users
import me.alexirving.pterobot.database.struct.Bot
import net.dv8tion.jda.api.entities.Member

@Command("server")
class Server(private val bot: Bot) : BaseCommand() {
    @Default
    fun server(e: SlashSender, @Optional member: Member?) {
        e.deferReply().queue()
        val guild = e.guild ?: return
        users.get(e.user.id) {
            it.getSession(bot, e) {
                it ?: return@getSession
                it.reloadCache{
                    e.hook.editOriginal("Servers:\n${it.cached.map { it.key }.joinToString("\n")}").queue()

                }
            }
        }
    }
}