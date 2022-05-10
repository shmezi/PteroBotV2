package me.alexirving.pterobot.database

import java.util.*

/**
 * Represents an async database.
 * All methods should have the prefix of db to show that this is for internal use and you should **NEVER** use it except in the userManager!
 */
interface Database<K : Any, T : Cacheable<K>> {
    /**
     * Reload the database
     * @param connection The connection string to use for the database
     */
    fun dbReload(connection: String)

    /**
     * Retrieve a data from the database
     */
    fun dbGet(id: String, async: (value: T?) -> Unit)

    /**
     * Update data in the database
     */
    fun dbUpdate(value: T)

    /**
     * Delete data in the database
     */
    fun dbDelete(key: UUID)
}
