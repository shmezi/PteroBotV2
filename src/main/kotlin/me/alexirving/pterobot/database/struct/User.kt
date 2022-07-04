package me.alexirving.pterobot.database.struct

import com.fasterxml.jackson.annotation.JsonIgnore
import me.alexirving.pterobot.database.Cacheable

class User(
    id: String = "", val bots: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

) : Cacheable<String>(id) {

    @JsonIgnore
    val sessions = mutableMapOf<String, MutableMap<String, PanelSession>>()

}