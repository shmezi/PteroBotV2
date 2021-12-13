package me.alexirving.bot.command.commands.server

import com.mattmalec.pterodactyl4j.PteroBuilder
import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import me.alexirving.bot.PteroBot
import me.alexirving.bot.utils.Config
import me.alexirving.bot.utils.getValue
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member

class SessionManager(val pteroBot: PteroBot, val configs: HashMap<Guild, Config>) {
    private val clientMap = HashMap<Member, PteroClient>()
    private val sessionMap = HashMap<Member, Session>()
    fun getClient(member: Member): PteroClient {
        val config = configs[member.guild]!!
        if (clientMap[member] == null)
            clientMap[member] = PteroBuilder.create(
                config.getValue("url"),
                getValue("${pteroBot.clientId}_keys", member.guild.id, member.id)
            )
                .setHttpClient(pteroBot.okHttpClient)
                .buildClient()
        return clientMap[member]!!
    }

    fun setClient(member: Member, client: PteroClient) {
        clientMap[member] = client
    }


    fun newSession(member: Member): Session {
        sessionMap[member] = Session(pteroBot, getClient(member), member)
        return sessionMap[member]!!
    }

    fun getSession(member: Member): Session {
        return if (sessionMap[member] == null)
            newSession(member)
        else sessionMap[member]!!
    }
}