package me.alexirving.pterobot.database.struct

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.triumphteam.cmd.slash.sender.SlashSender
import me.alexirving.pterobot.buildClientSafely
import me.alexirving.pterobot.database.Cacheable

class User(
    id: String = "", val bots: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

) : Cacheable<String>(id) {

    @JsonIgnore
    private val sessions = mutableMapOf<String, MutableMap<String, PanelSession>>()

    fun Bot.getKey(guild: String) = bots[this.identifier]?.get(guild) ?: ""

    @JsonIgnore
    private fun newSession(bot: Bot, e: SlashSender, async: (session: PanelSession?) -> Unit) {
        val egID = e.guild?.id
        if (egID == null) {
            async(null)
            return
        }
        buildClientSafely(bot.getValue(egID, GuildSetting.URL), bot.getKey(egID)) {
            if (it == null) {
                async(null)
                return@buildClientSafely
            }
            val ps = PanelSession(it, e.hook)
            sessions.getOrPut(bot.identifier) { mutableMapOf() }[egID] = ps
            async(ps)
        }
    }

    @JsonIgnore
    fun getSession(bot: Bot, e: SlashSender, async: (session: PanelSession?) -> Unit) {
        val egID = e.guild?.id
        if (egID == null) {
            async(null)
            return
        }
        if (sessions[bot.identifier]?.containsKey(egID) == true)
            sessions[bot.identifier]?.get(egID) ?: return
        else
            newSession(bot, e) {
                async(it)
            }
    }
}