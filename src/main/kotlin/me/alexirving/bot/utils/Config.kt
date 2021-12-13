package me.alexirving.bot.utils

import me.alexirving.bot.PteroBot
import net.dv8tion.jda.api.entities.Guild

class Config(val client: PteroBot, val guild: Guild) {
    private val configItems = HashMap<String, String>()

    init {
        reloadConfig()
    }

    fun reloadConfig() {
        val ps = getRow("${client.clientId}_configs", guild.id)
        for (i: String in getColumnNames("${client.clientId}_configs").drop(1))

            configItems[i] = if (ps != null)
                if (ps.getString(i) != null)
                    ps.getString(i)
                else
                    "default"
            else
                "default"

    }

    fun getValue(key: String): String {
        return configItems[key]!!
    }

}