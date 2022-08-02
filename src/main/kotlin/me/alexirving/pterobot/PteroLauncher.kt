package me.alexirving.pterobot

import me.alexirving.lib.database.nosql.MongoConnection
import me.alexirving.lib.database.nosql.MongoDbCachedCollection
import me.alexirving.lib.database.nosql.MongoUtils
import me.alexirving.pterobot.PteroLauncher.bots
import me.alexirving.pterobot.PteroLauncher.clients
import me.alexirving.pterobot.PteroLauncher.settings
import me.alexirving.pterobot.PteroLauncher.users
import me.alexirving.pterobot.struct.Bot
import me.alexirving.pterobot.struct.Client
import me.alexirving.pterobot.struct.User
import java.io.FileReader
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

object PteroLauncher {
    val settings = Properties().apply { load(FileReader("bot.properties")) }
    private val connection = if (settings.getProperty("AUTHENTICATE") != "TRUE")
        MongoConnection(
            settings.getProperty("CONNECTION"), "PteroBotV4"
        )
    else MongoConnection(
        MongoUtils.defaultClient(
            settings.getProperty("CONNECTION"),
            settings.getProperty("DB"),
            settings.getProperty("USER"),
            settings.getProperty("PASS")
        ), "PteroBotV4"
    )
    val clients = MongoDbCachedCollection("Clients", Client::class.java, connection).getManager(Client())
    val users = MongoDbCachedCollection("Users", User::class.java, connection).getManager(User())
    val bots = MongoDbCachedCollection("Bots", Bot::class.java, connection).getManager(
        Bot(dontBuildBot = true)
    )
}

fun main() {
    copyOver("bot.properties")
    bots.get(settings.getProperty("ID"), true) {
    }
    Timer().scheduleAtFixedRate(0L, settings.getProperty("RELOAD").toLong()) {
        clients.update()
        users.update()
        bots.update()
    }
    Runtime.getRuntime().addShutdownHook(
        Thread {
            "Disabling bot, do not force kill!".color(Colors.RED).pq()
            clients.update()
            users.update()
            bots.update()
        }
    )


}

