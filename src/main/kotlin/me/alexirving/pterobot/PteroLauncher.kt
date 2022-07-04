package me.alexirving.pterobot

import me.alexirving.pterobot.PteroLauncher.bots
import me.alexirving.pterobot.database.nosql.MongoConnection
import me.alexirving.pterobot.database.nosql.MongoDbCachedCollection
import me.alexirving.pterobot.database.struct.Bot
import me.alexirving.pterobot.database.struct.Client
import me.alexirving.pterobot.database.struct.GuildSetting
import me.alexirving.pterobot.database.struct.User
import java.io.FileReader
import java.util.*

object PteroLauncher {
    val settings = Properties().apply { load(FileReader("bot.properties")) }
    private val connection = MongoConnection(settings.getProperty("CONNECTION"), "PteroBotV4")
    val clients = MongoDbCachedCollection("Clients", Client(), connection).getManager()
    val users = MongoDbCachedCollection("Users", User(), connection).getManager()
    val bots = MongoDbCachedCollection("Bots", Bot(template = true), connection).getManager()
}

fun main() {
    copyOver("bot.properties")
    bots.get("866090776747180032", false) {}


}

