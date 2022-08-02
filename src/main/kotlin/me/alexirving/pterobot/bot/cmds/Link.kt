package me.alexirving.pterobot.bot.cmds

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.ArgName
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.Description
import dev.triumphteam.cmd.slash.sender.SlashSender
import me.alexirving.pterobot.PteroLauncher.users
import me.alexirving.pterobot.buildClientSafely
import me.alexirving.pterobot.struct.Bot
import me.alexirving.pterobot.struct.GuildSetting

@Command("link")
class Link(private val bot: Bot) : BaseCommand() {


    @Default
    @Description("Link your panel account")
    fun link(e: SlashSender, @ArgName("apikey") @Description("Your API key from the panel") apikey: String) {
        val eg = e.guild
        val em = e.member
        if (eg == null || em == null) {
            e.reply("Commands must be run inside a guild!").setEphemeral(true).queue()
            return
        }
        e.deferReply(true).queue()
        buildClientSafely(bot.getString(eg.id, GuildSetting.URL), apikey) { client ->
            client?.retrieveAccount()?.executeAsync { pteroAccount ->
                e.hook.editOriginal("You have been linked to the account with the name: ${pteroAccount.firstName}")
                    .queue()

                users.get(e.user.id, true) {
                    it.bots[bot.identifier] = mutableMapOf<String, String>().apply { this[eg.id] = apikey }
                    eg.addRoleToMember(
                        em,
                        eg.getRoleById(bot.getString(eg.id, GuildSetting.LINKED) ?: return@get) ?: return@get
                    ).queue()
                }
            }
        }
    }
}