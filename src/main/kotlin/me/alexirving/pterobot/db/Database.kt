package me.alexirving.pterobot.db

import me.alexirving.pterobot.db.maps.UserData
import java.sql.Connection

interface Database {
    fun reload(connection: Connection)
    fun getUser(async: (userData: UserData) -> Unit)

}