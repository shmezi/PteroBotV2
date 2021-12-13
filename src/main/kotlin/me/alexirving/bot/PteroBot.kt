package me.alexirving.bot

import me.alexirving.bot.command.CommandHandler
import me.alexirving.bot.command.commands.Help
import me.alexirving.bot.command.commands.Link
import me.alexirving.bot.command.commands.server.Server
import me.alexirving.bot.command.commands.server.SessionManager
import me.alexirving.bot.events.MenuSelect
import me.alexirving.bot.events.Startup
import me.alexirving.bot.utils.Config
import me.alexirving.bot.utils.addColumn
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import okhttp3.OkHttpClient

class PteroBot(private val token: String, val clientId: String, val okHttpClient: OkHttpClient) {
    val commandHandler = CommandHandler()
    val configs = HashMap<Guild, Config>()
    val sessionManager = SessionManager(this, configs)

    fun start() {
        val jda = JDABuilder.createDefault(token)
        jda.addEventListeners(commandHandler, Startup(this), MenuSelect())
        commandHandler.registerCommands(
            Help(commandHandler),
            Server(sessionManager),
            Link(this)
        )
        addColumn("${clientId}_configs", "url", null)
        jda.build()


    }
}
