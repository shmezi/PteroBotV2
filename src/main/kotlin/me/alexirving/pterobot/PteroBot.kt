package me.alexirving.pterobot

import com.mattmalec.pterodactyl4j.PteroBuilder
import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import dev.triumphteam.cmd.slash.SlashCommandManager
import me.alexirving.pterobot.cmds.Link
import me.alexirving.pterobot.database.CachedManager
import me.alexirving.pterobot.database.nosql.MongoDbCachedDatabase
import me.alexirving.pterobot.database.structs.UserData
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import okhttp3.OkHttpClient
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.concurrent.timerTask
val config = Properties().apply {
    if (!File("bot.properties").exists())
        Files.copy(
            Thread.currentThread().contextClassLoader.getResourceAsStream("bot.properties"),
            Path.of("bot.properties")
        )
    load(FileInputStream("bot.properties"))
}
class PteroBot {
    val scheduler = Timer("PteroBot")
    private val connection: String = config.getProperty("CONNECTION")
    val okHttp = OkHttpClient()
    private val url: String = config.getProperty("URL")
    val aapi = PteroBuilder.create(url, config.getProperty("APIKEY")).setHttpClient(okHttp)
        .buildApplication()

    val users = CachedManager(
        MongoDbCachedDatabase("UserData", UserData::class.java, connection), UserData(
            mutableMapOf()
        )
    )
    private val jda =
        JDABuilder.create(config.getProperty("TOKEN"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).build()
    private val cmm = SlashCommandManager.create(jda)
    fun client(api: String): PteroClient? {
        return try {
            PteroBuilder.create(url, api).setHttpClient(okHttp).buildClient()
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    init {
        scheduler.scheduleAtFixedRate(timerTask {
            users.update()
        },  config.getProperty("REFRESH").toLong(), config.getProperty("REFRESH").toLong())
            cmm.registerCommand(Link(this))
    }

}



fun main() {
    PteroBot()
}