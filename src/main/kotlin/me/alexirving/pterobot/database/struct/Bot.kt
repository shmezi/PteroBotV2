package me.alexirving.pterobot.database.struct

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.triumphteam.cmd.slash.SlashCommandManager
import me.alexirving.pterobot.bot.cmds.Link
import me.alexirving.pterobot.bot.cmds.Server
import me.alexirving.pterobot.database.Cacheable
import me.alexirving.pterobot.database.CachedDbManager
import net.dv8tion.jda.api.JDABuilder

class Bot(
    id: String = "",
    var token: String = "",
    val guilds: MutableMap<String, MutableMap<GuildSetting, String>> = mutableMapOf(),
    template: Boolean = false
) : Cacheable<String>(id) {
    var userDb: CachedDbManager<String, User>? = null

    @JsonIgnore
    private val bot = if (template) null else JDABuilder.createDefault(token).build()

    @JsonIgnore
    private val cmds = if (bot == null || template) null else SlashCommandManager.create(bot)

    init {
        cmds?.registerCommand(Link(this), Server(this))
    }

    override fun clone(): Bot {
        return (super.clone() as Bot).apply { this.userDb = this@Bot.userDb }
    }
}