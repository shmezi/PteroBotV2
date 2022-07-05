package me.alexirving.pterobot.database.struct

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import net.dv8tion.jda.api.interactions.InteractionHook

class PanelSession(
    private val client: PteroClient,
    private val hook: InteractionHook
) {
    val cached = mutableMapOf<String, ClientServer>()

    init {
        reloadCache {}
    }

    fun reloadCache(done: () -> Unit) {
        client.retrieveServers().executeAsync {
            for (s in it)
                cached[s.identifier] = s
            done()
        }
    }

    fun updatePanel() {
        hook.editOriginal("Shoot shmezi rn").queue()
    }

    operator fun get(key: String) = cached[key]
    operator fun inc(): PanelSession {
        reloadCache{}; return this
    }
}