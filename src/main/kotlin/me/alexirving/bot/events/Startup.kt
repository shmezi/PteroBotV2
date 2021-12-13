package me.alexirving.bot.events

import me.alexirving.bot.PteroBot
import me.alexirving.bot.utils.Config
import me.alexirving.bot.utils.addColumn
import me.alexirving.bot.utils.addRow
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class Startup(val pteroBot: PteroBot) : ListenerAdapter() {
    override fun onGuildReady(event: GuildReadyEvent) {
        val guild = event.guild
        val guildId = guild.id
        guild.updateCommands().addCommands(pteroBot.commandHandler.getAsCommands()).queue()
        pteroBot.configs[guild] = Config(pteroBot, guild)
         addColumn("${pteroBot.clientId}_keys", guildId, null)
        addRow("${pteroBot.clientId}_configs", guildId)
        println("I'm inside the guild: ${guild.name} with the id of: $guildId")
    }
}