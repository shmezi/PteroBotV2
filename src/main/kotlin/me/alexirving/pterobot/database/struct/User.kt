package me.alexirving.pterobot.database.struct

import me.alexirving.pterobot.database.Cacheable
import net.dv8tion.jda.api.entities.Guild

class User(
    id: String = "", val bots: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

) : Cacheable<String>(id) {
    fun getUser(botId: String, guild: Guild) = bots[botId]?.get(guild.id)
}