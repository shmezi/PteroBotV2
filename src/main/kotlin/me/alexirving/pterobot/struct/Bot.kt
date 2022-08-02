package me.alexirving.pterobot.struct

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mattmalec.pterodactyl4j.PteroBuilder
import com.mattmalec.pterodactyl4j.application.entities.PteroApplication
import dev.triumphteam.cmd.slash.SlashCommandManager
import me.alexirving.lib.database.Cacheable
import me.alexirving.pterobot.bot.cmds.Link
import me.alexirving.pterobot.bot.cmds.Server
import me.alexirving.pterobot.bot.cmds.Setup
import me.alexirving.pterobot.bot.cmds.Test
import me.alexirving.pterobot.embed.RawEmbed
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Member
import javax.security.auth.login.LoginException


class Bot(
    id: String = "",
    /**
     * PLEASE DO NOT REMOVE VAL, OTHERWISE DB DOES NOT FUCKING SAVE IT U FUCKING MORON IDIOT GO FUCK URSELF IDIOT
     */
    var token: String = "",
    val settings: MutableMap<String, MutableMap<GuildSetting, String>> = mutableMapOf(),
    val embeds: MutableMap<String, MutableMap<GuildSetting, RawEmbed>> = mutableMapOf(),
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
        val settings = settings[guild] ?: return null
        return PteroBuilder.createApplication(
            settings[GuildSetting.URL] as String,
            settings[GuildSetting.API] as String
        )
    }

    fun getString(guild: String, setting: GuildSetting) = settings[guild]?.get(setting)
    fun getEmbed(guild: String, setting: GuildSetting) = embeds[guild]?.get(setting)

    @JsonIgnore
    private val cmds = if (bot == null || template) null else SlashCommandManager.create(bot).apply {
        this.registerArgument(Member::class.java) { sender, args ->
            sender.member?.guild?.retrieveMemberById(args)?.complete()

        }


    }

    init {
        cmds?.registerCommand(Link(this), Server(this), Setup(this), Test(this))
        bot?.addEventListener(InteractionListeners(this))
    }
}