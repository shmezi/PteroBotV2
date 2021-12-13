package me.alexirving.bot.command.commands.server

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import me.alexirving.bot.PteroBot
import me.alexirving.bot.utils.getStatus
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu

class Session(
    val pteroBot: PteroBot, val client: PteroClient, val member: Member
) {

    private var servers: List<ClientServer>? = null
    private var utilizations = HashMap<ClientServer, Utilization>()
    private val embedBuilder = EmbedBuilder()
    private var menu: ActionRow? = null
    private var selected: ClientServer? = null
    private var selecter = 0
    private var sessionMessage: Message? = null

    private fun start(done: (Session) -> Unit) {

        client.retrieveServers().executeAsync { clientServers ->
            servers = clientServers
            selected = servers!![0]
            var counter = 1
            servers!!.forEach {
                it.retrieveUtilization().executeAsync { utilization ->
                    utilizations[it] = utilization

                    if (counter >= servers!!.size)
                        done(this)
                    else
                        counter++
                    println("$counter VS ${servers!!.size}")
                }
            }

        }

        embedBuilder.setTitle("${member.effectiveName}'s Server Manager")
    }

    fun fullyLoad(done: (Session) -> Unit) {
        start {
            menu = ActionRow.of(
                SelectionMenu.create("${pteroBot.clientId}:${member.guild.id}:${member.id}")
                    .addOptions(getAsOptions()).build()
            )
            done(it)
        }
    }


    fun getSessionPanel(): EmbedBuilder {
        val temp = EmbedBuilder(embedBuilder)
        for (server: ClientServer in servers!!) {
            if (utilizations[server] == null)
                println("${server.name} is null!")
            if (server == selected)
                temp.appendDescription("${getStatus(utilizations[server]!!)}${server.name}\n")
        }
        return temp
    }

    fun getAsOptions(): List<SelectOption> {
        val t = ArrayList<SelectOption>()
        servers!!.forEach {
            t.add(
                SelectOption.of(
                    it.name,
                    it.identifier
                )
            ); println("Added another option! ${it.name} | ${it.identifier}")
        }
        return t
    }

    fun getSessionSelector(): ActionRow {
        return menu!!
    }

    fun select(serverName: String) {

    }
}
