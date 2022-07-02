package me.alexirving.pterobot

import me.alexirving.pterobot.database.nosql.MongoConnection
import me.alexirving.pterobot.database.nosql.MongoDbCachedCollection
import me.alexirving.pterobot.database.struct.Bot
import me.alexirving.pterobot.database.struct.Client
import me.alexirving.pterobot.database.struct.GuildSetting
import me.alexirving.pterobot.database.struct.User
import java.io.FileReader
import java.util.*

fun main() {

    copyOver("bot.properties")
    val settings = Properties()
    settings.load(FileReader("bot.properties"))
    val connection = MongoConnection(settings.getProperty("CONNECTION"), "PteroBotV4")
    val clientDb = MongoDbCachedCollection("Clients", Client(), connection).getManager()
    val userDb = MongoDbCachedCollection("Users", User(), connection).getManager()
    val botDb =
        MongoDbCachedCollection("Bots", Bot(template = true).apply {
            this.userDb = userDb
                                                                   }, connection).getManager()


    botDb.get("866090776747180032", true) {
        it.guilds["939537486160482354"] = mutableMapOf<GuildSetting, String>().apply {
            this[GuildSetting.URL] = "https://panel.typicalhost.net/"
        }
    }


}

