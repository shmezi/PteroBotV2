package me.alexirving.pterobot.database.struct

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mattmalec.pterodactyl4j.PteroBuilder
import com.mattmalec.pterodactyl4j.application.entities.PteroApplication
import dev.triumphteam.cmd.slash.SlashCommandManager
import me.alexirving.pterobot.bot.cmds.Link
import me.alexirving.pterobot.bot.cmds.Server
import me.alexirving.pterobot.bot.cmds.Setup
import me.alexirving.pterobot.database.Cacheable
import net.dv8tion.jda.api.JDABuilder
import javax.security.auth.login.LoginException

class Bot(
    id: String = "",
    val token: String = "",
    val guilds: MutableMap<String, MutableMap<GuildSetting, String>> = mutableMapOf(),
    template: Boolean = false
) : Cacheable<String>(id) {

    @JsonIgnore
    private val bot = if (template) null else try {
        JDABuilder.createDefault(token).build()
    } catch (_: LoginException) {
        println("The bot token for bot ID: \"${id}\" is invalid or missing :(")
        null
    }

    @JsonIgnore
    fun getApplication(guild: String): PteroApplication? {
        val settings = guilds[guild] ?: return null
        return PteroBuilder.createApplication(settings[GuildSetting.URL], settings[GuildSetting.API])
    }

    fun getValue(guild: String, setting: GuildSetting) = guilds[guild]?.get(setting) ?: ""

    @JsonIgnore
    private val cmds = if (bot == null || template) null else SlashCommandManager.create(bot)

    init {
        cmds?.registerCommand(Link(this), Server(this), Setup(this))
    }
}