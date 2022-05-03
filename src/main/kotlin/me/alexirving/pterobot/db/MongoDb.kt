package me.alexirving.pterobot.db

import kotlinx.coroutines.runBlocking
import me.alexirving.pterobot.db.maps.GuildData
import me.alexirving.pterobot.db.maps.UserData
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.sql.Connection

class MongoDb : Database {
    var db: CoroutineDatabase? = null
    val udb = db?.getCollection<UserData>("UserData")
    val gdb = db?.getCollection<GuildData>("GuildData")

    init {
        System.setProperty(
            "org.litote.mongo.test.mapping.service",
            "org.litote.kmongo.jackson.JacksonClassMappingTypeService"
        )
    }

    override fun reload(connection: Connection) {
        db = KMongo.createClient().coroutine.getDatabase("PteroBot")
       runBlocking {
           udb?.ensureUniqueIndex(UserData::userId)
           gdb?.ensureUniqueIndex(GuildData::guildId)
       }
    }

    override fun getUser(async: (userData: UserData) -> Unit) {
        runBlocking {

        }
    }


}