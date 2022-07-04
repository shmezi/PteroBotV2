package me.alexirving.pterobot.bot.cmds

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.SubCommand
import dev.triumphteam.cmd.slash.sender.SlashSender
import me.alexirving.pterobot.PteroLauncher.bots
import me.alexirving.pterobot.database.struct.Bot
import me.alexirving.pterobot.database.struct.GuildSetting
import me.alexirving.pterobot.validateAdmin

@Command("setup")
class Setup(private val bot: Bot) : BaseCommand() {


    @SubCommand("setup")
    fun setup(e: SlashSender, setting: GuildSetting, value: String) {
        validateAdmin(e) { scope ->
            bots.get(bot.identifier, true) {
                it.guilds[e.guild?.id ?: return@get]?.set(setting, value)
                scope.message("Changed config value for guild!")
            }
        }


    }

    @SubCommand("info")
    fun view(e: SlashSender) {
        validateAdmin(e) { scope ->
            val c = StringBuilder("Set config options:")
            bots.get(bot.identifier) { bot ->
                bot.guilds[scope.guild.id]?.forEach {
                    c.append("\n${it.key} - ||${it.value}||")
                }
            }
            scope.message(c.toString())
        }
    }
}