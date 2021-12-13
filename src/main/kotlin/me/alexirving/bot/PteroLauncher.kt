package me.alexirving.bot

import me.alexirving.bot.utils.createTables
import me.alexirving.bot.utils.initDb
import okhttp3.OkHttpClient
import org.yaml.snakeyaml.Yaml
import java.io.FileInputStream

fun main() {
    val yaml = Yaml()
    val bots = ArrayList<PteroBot>()
    val okHttpClient = OkHttpClient()

    val configMap: Map<String, Any> = yaml.load(FileInputStream("config.yml"))
    val sqlConfig: Map<String, Any> = configMap["Sql"] as Map<String, Any>

    copyIfNotExist("config.yml")

    initDb(
        sqlConfig["Host"] as String,
        sqlConfig["Port"] as Int,
        sqlConfig["Database"] as String,
        sqlConfig["Username"] as String,
        sqlConfig["Password"] as String
    )


    val tables = ArrayList<String>()
    for (clientId: String in (configMap["Tokens"] as HashMap<String, String>).keys) {

        tables.add("${clientId}_configs"); tables.add("${clientId}_keys")
        println(clientId)
    }
    createTables(tables)
    println(tables)

    for (clientId: String in (configMap["Tokens"] as HashMap<String, String>).keys)
        bots.add(PteroBot((configMap["Tokens"] as HashMap<String, String>)[clientId]!!, clientId, okHttpClient))

    for (bot: PteroBot in bots)
        bot.start()
}
