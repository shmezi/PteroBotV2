package me.alexirving.pterobot.struct

import me.alexirving.lib.database.Cacheable

class Client(
    id: String = "",
    val ownership: MutableSet<String> = mutableSetOf()

) : Cacheable<String>(id)