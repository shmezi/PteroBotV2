package me.alexirving.pterobot.database.nosql

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.ReplaceOptions
import kotlinx.coroutines.runBlocking
import me.alexirving.pterobot.database.Cacheable
import me.alexirving.pterobot.database.Database
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo
import org.litote.kmongo.ensureUniqueIndex
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import java.util.*

class MongoDbCachedDatabase<ID : Any, T : Cacheable<ID>>(val id: String, val type: Class<T>, connection: String) :
    Database<ID, Cacheable<ID>> {
    lateinit var client: MongoClient
    private lateinit var edb: MongoDatabase
    private lateinit var ec: MongoCollection<T>

    init {
        System.setProperty(
            "org.litote.mongo.test.mapping.service",
            "org.litote.kmongo.jackson.JacksonClassMappingTypeService"
        )
        dbReload(connection)
    }

    override fun dbReload(connection: String) {
        client = KMongo.createClient(
            MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(ConnectionString(connection)).build()
        )
        edb = client.getDatabase("McEngine")
        ec = edb.getCollection(id, type)
        runBlocking {
            ec.ensureUniqueIndex(Cacheable<ID>::identifier)

        }
    }

    override fun dbDelete(key: UUID) {
        runBlocking {
            ec.deleteOne(Cacheable<ID>::identifier eq key.toString())

        }
    }

    override fun dbUpdate(value: Cacheable<ID>) {
        runBlocking {
            ec.replaceOne(Cacheable<ID>::identifier eq value.identifier, value as T, ReplaceOptions().upsert(true))
        }

    }

    override fun dbGet(id: String, async: (value: Cacheable<ID>?) -> Unit) {
        runBlocking {
            async(ec.findOne(Cacheable<ID>::identifier eq id))

        }
    }


}