package me.alexirving.pterobot.bot.cmds

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.*
import dev.triumphteam.cmd.slash.sender.SlashSender
import me.alexirving.pterobot.PteroLauncher.users
import me.alexirving.pterobot.struct.Bot
import me.alexirving.pterobot.validateAdmin
import net.dv8tion.jda.api.entities.Member

@Command("server")
class Server(private val bot: Bot) : BaseCommand() {
    @Default
    @Description("Create new Panel Session")
    fun server(e: SlashSender, @ArgName("member") @Optional member: Member?) {
        e.guild ?: return
        if (!validateAdmin(bot, e.event) && member != null) {
            return
        }
        users.get(e.user.id) { user ->
            user.newSession(bot, member ?: e.member ?: return@get, e.hook) {
                if (it == null) {
                    e.hook.editOriginal("User is not linked!").queue()
                } else {
                    it.reloadCache {
                        it.updatePanel(true)
                    }
                }
            }
        }
    }
}