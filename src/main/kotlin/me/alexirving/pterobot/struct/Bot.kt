package me.alexirving.pterobot.struct

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mattmalec.pterodactyl4j.PteroBuilder
import com.mattmalec.pterodactyl4j.application.entities.PteroApplication
import dev.triumphteam.cmd.slash.SlashCommandManager
import me.alexirving.lib.database.Cacheable
import me.alexirving.pterobot.PteroLauncher
import me.alexirving.pterobot.bot.cmds.Link
import me.alexirving.pterobot.bot.cmds.Server
import me.alexirving.pterobot.bot.cmds.Setup
import me.alexirving.pterobot.embed.RawEmbed
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Member


class Bot(
    id: String = "",
    /**
     * PLEASE DO NOT REMOVE VAL, OTHERWISE DB DOES NOT FUCKING SAVE IT U FUCKING MORON IDIOT GO FUCK URSELF IDIOT
     */
    var token: String = "",
    val settings: MutableMap<String, MutableMap<GuildSetting, String>> = mutableMapOf(),
    val embeds: MutableMap<String, MutableMap<GuildSetting, RawEmbed>> = mutableMapOf(),
    dontBuildBot: Boolean = false
) : Cacheable<String>(id) {

    //Fixme: I replaced with a cool token yay
    @JsonIgnore
    private val bot =
        if (dontBuildBot) null else JDABuilder.createDefault(PteroLauncher.settings.getProperty("TOKEN")).build()

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
    val cmds = if (dontBuildBot) null else SlashCommandManager.create(bot!!)

    init {
        bot?.addEventListener(InteractionListeners(this))
        cmds?.registerCommand(Link(this), Server(this), Setup(this))
        try {
            cmds?.updateAllCommands()
        } catch (_: Exception) {
        }
    }

    override fun clone(): Bot {
        return Bot()
    }
}
