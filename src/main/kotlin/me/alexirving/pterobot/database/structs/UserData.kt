package me.alexirving.pterobot.database.structs

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import me.alexirving.pterobot.database.Cacheable

data class UserData(
    val guilds: MutableMap<String, String>
) : Cacheable<String>() {
    override fun clone(): Cacheable<String> {

        return super.clone()
    }

    @JsonIgnore
    private val session: MutableMap<String, ClientServer>? = null

    @JsonIgnore
    fun getSession(id: String) = session?.get(id)

    @JsonIgnore
    fun reloadCache() {
    }
}