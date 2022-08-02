package me.alexirving.pterobot

import me.alexirving.lib.database.nosql.MongoConnection
import me.alexirving.lib.database.nosql.MongoDbCachedCollection
import me.alexirving.lib.database.nosql.MongoUtils.defaultClient
import me.alexirving.pterobot.PteroLauncher.bots
import me.alexirving.pterobot.struct.Bot
import me.alexirving.pterobot.struct.Client
import me.alexirving.pterobot.struct.User
import java.io.FileReader
import java.util.*

object PteroLauncher {
    val settings = Properties().apply { load(FileReader("bot.properties")) }
    private val connection = MongoConnection(
        defaultClient(
            settings.getProperty("CONNECTION"),
            settings.getProperty("USER"),
            settings.getProperty("PASS")
        ), "PteroBotV4"
    )
    val clients = MongoDbCachedCollection("Clients", Client(), connection).getManager()
    val users = MongoDbCachedCollection("Users", User(), connection).getManager()
    val bots = MongoDbCachedCollection("Bots", Bot(template = true), connection).getManager()
}

fun main() {
    copyOver("bot.properties")
    bots.get(PteroLauncher.settings.getProperty("ID"), true) {
        it.token = PteroLauncher.settings.getProperty("TOKEN")
    }


}

