package me.alexirving.pterobot.bot.cmds

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.slash.sender.SlashSender
import me.alexirving.pterobot.PteroLauncher.bots
import me.alexirving.pterobot.PteroLauncher.users
import me.alexirving.pterobot.buildClientSafely
import me.alexirving.pterobot.database.struct.Bot
import me.alexirving.pterobot.database.struct.GuildSetting
import me.alexirving.pterobot.validateAdmin
import net.dv8tion.jda.api.entities.Member

@Command("access")
class Access(private val bot: Bot) : BaseCommand() {


    @Default
    fun access(e: SlashSender, email: String, of: Member) {
        validateAdmin(e) { scope ->
            users.get(of.id) { user ->
                bots.get(bot.identifier) { bot ->
                    val settings = bot.guilds[e.guild?.id ?: return@get] ?: return@get

                    buildClientSafely(
                        settings[GuildSetting.URL],
                        user.bots[this.bot.identifier]?.get(e.guild?.id) ?: return@get
                    ) { client ->
                        client ?: return@buildClientSafely
                        client.retrieveServers().executeAsync {
                            it.forEach { s ->
                                s.subuserManager.createUser().setEmail(email)
                            }
                        }
                    }
                }
            }
        }


    }
}