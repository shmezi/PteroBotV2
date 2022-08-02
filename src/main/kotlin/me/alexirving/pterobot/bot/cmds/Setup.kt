package me.alexirving.pterobot.bot.cmds

import com.google.gson.Gson
import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.ArgName
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Description
import dev.triumphteam.cmd.core.annotation.SubCommand
import dev.triumphteam.cmd.slash.sender.SlashSender
import me.alexirving.pterobot.PteroLauncher.bots
import me.alexirving.pterobot.embed.RawEmbed
import me.alexirving.pterobot.struct.Bot
import me.alexirving.pterobot.struct.GuildSetting
import me.alexirving.pterobot.struct.SettingType
import me.alexirving.pterobot.validateAdmin

@Command("setup")
class Setup(private val bot: Bot) : BaseCommand() {

    private val g = Gson()


    @SubCommand("setup")
    @Description("Set up your PteroBot")
    fun setup(
        e: SlashSender,
        @ArgName("setting") @Description("The setting to set") setting: GuildSetting,
        @ArgName("value") @Description("The value to set the setting to") value: String
    ) {

        if (validateAdmin(bot, e.event))
            bots.get(bot.identifier, true) {
                when (setting.type) {
                    SettingType.STRING -> it.settings.getOrPut(e.guild?.id ?: return@get) { mutableMapOf() }[setting] =
                        value

                    SettingType.EMBED -> it.embeds.getOrPut(e.guild?.id ?: return@get) { mutableMapOf() }[setting] =
                        g.fromJson(value, RawEmbed::class.java)

                }
                e.hook.editOriginal("Changed config value for guild!").queue()
            }


    }

    @SubCommand("info")
    fun view(e: SlashSender) {
        if (validateAdmin(bot, e.event)) {
            val c = StringBuilder("Set config options:")
            bots.get(bot.identifier) { bot ->
                bot.settings[e.guild?.id]?.forEach {
                    c.append("\n${it.key} - ||${it.value}||")
                }
            }
            e.hook.editOriginal((c.toString())).queue()
        }
    }
}