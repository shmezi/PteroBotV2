package me.alexirving.pterobot.bot.cmds

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.slash.sender.SlashSender
import me.alexirving.pterobot.PteroLauncher.users
import me.alexirving.pterobot.buildClientSafely
import me.alexirving.pterobot.database.struct.Bot
import me.alexirving.pterobot.database.struct.GuildSetting

@Command("link")
class Link(private val bot: Bot) : BaseCommand() {


    @Default
    fun link(e: SlashSender, apikey: String) {
        val eg = e.guild
        if (eg == null) {
            e.reply("Commands must be run inside a guild!").setEphemeral(true).queue()
            return
        }
        e.deferReply().queue()
        buildClientSafely(bot.getValue(eg.id, GuildSetting.URL), apikey) { client ->
            client?.retrieveAccount()?.executeAsync { pteroAccount ->
                e.hook.editOriginal("You have been linked to the account with the name: ${pteroAccount.firstName}")
                    .queue()

                users.get(e.user.id, true) {
                    it.bots[bot.identifier] = mutableMapOf<String, String>().apply { this[eg.id] = apikey }
                }
            }
        }
    }
}