package me.alexirving.pterobot.database.struct

import me.alexirving.pterobot.database.Cacheable
class Client(
    id: String = "",
    val ownership: MutableSet<String> = mutableSetOf()

) : Cacheable<String>(id)