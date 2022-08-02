package me.alexirving.pterobot.struct

import com.fasterxml.jackson.annotation.JsonIgnore
import me.alexirving.lib.database.Cacheable
import me.alexirving.pterobot.PteroLauncher
import me.alexirving.pterobot.buildClientSafely
import me.alexirving.pterobot.pq
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.interactions.InteractionHook

class User(
    id: String = "",
    val bots: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

) : Cacheable<String>(id) {

    @JsonIgnore
    private val sessions = mutableMapOf<String, MutableMap<String, PanelSession>>()

    fun Bot.getKeyForSelf(guild: String) = bots[identifier]?.get(guild) ?: ""

    fun Bot.getKey(user: String, guild: String, async: (key: String) -> Unit) {
        PteroLauncher.users.get(user) {
            async(it.bots[this.identifier]?.get(guild) ?: "")
        }
    }


    /**
     * Creates a new panel session for a member in a guild.
     * @param bot The bot this affects
     */
    @JsonIgnore
    fun newSession(bot: Bot, member: Member, hook: InteractionHook, async: (session: PanelSession?) -> Unit) {
        val egID = member.guild.id
        bot.getKey(member.id, egID) { key ->
            buildClientSafely(bot.getString(egID, GuildSetting.URL), key) {
                if (it == null) {
                    async(null)
                    return@buildClientSafely
                }

                val ps = PanelSession(bot, it, member, hook)
                sessions.getOrPut(bot.identifier) { mutableMapOf() }[egID] = ps
                async(ps)
            }
        }

    }

    @JsonIgnore
    fun getSession(bot: Bot, member: Member, hook: InteractionHook, async: (session: PanelSession?) -> Unit) {
        val egID = member.guild.id
        if (sessions[bot.identifier]?.containsKey(egID) == true)
            sessions[bot.identifier]?.get(egID) ?: return
        else
            newSession(bot, member, hook) {
                async(it)
            }
    }

    @JsonIgnore
    fun getCachedSession(bot: Bot, guildId: String, async: (session: PanelSession?) -> Unit) {

        if (sessions[bot.identifier]?.containsKey(guildId) == true)
            async(sessions[bot.identifier]?.get(guildId) ?: return)

    }
}