package me.alexirving.pterobot.db.maps

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

data class UserData(
    val userId: String,
    val servers: MutableMap<String, String> //GuildId | Token
) {

    @JsonIgnore
    fun getId() = UUID.fromString(userId)
}