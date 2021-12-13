package me.alexirving.bot.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.PreparedStatement
import java.sql.ResultSet

private var config = HikariConfig()
private var ds: HikariDataSource = HikariDataSource()

fun initDb(host: String, port: Int, database: String, username: String, password: String) {
    config.jdbcUrl = "jdbc:mysql://${host}:${port}/${database}"
    config.username = username
    config.password = password
    config.maximumPoolSize = 1000
    ds = HikariDataSource(config)

}

fun initDb(database: String, username: String, password: String) {
    config.jdbcUrl = "jdbc:xerial:${database}"
    config.username = username
    config.password = password
    config.maximumPoolSize = 1000
    ds = HikariDataSource(config)

}


fun prepareStatement(statement: String): PreparedStatement {
    return ds.connection.prepareStatement(statement)
}

fun setValue(table: String, where: String, key: String, value: String) {
    prepareStatement("UPDATE `$table` SET `$key` = '${value}' WHERE id = '$where';").executeUpdate()
}


fun getValue(table: String, key: String, where: String): String? {
    val prep = prepareStatement("SELECT * FROM `$table` WHERE id = '${where}';").executeQuery()
    return if (prep.next())
        prep.getString(key)
    else null
}

fun getRow(table: String, where: String): ResultSet? {
    val prep = prepareStatement("SELECT * FROM `$table` WHERE id = '${where}';").executeQuery()
    return if (prep.next())
        prep
    else null
}

/**
 * Add
 */
fun addColumn(table: String, name: String, default: Any?) {
    var type = ""
    var defaultValue = ""
    when (default) {
        null -> type = "VARCHAR(120)"
        is Int -> {
            type = "INT(120)"
            defaultValue = " NOT NULL DEFAULT '$default'"
        }
        is String -> {
            type = "VARCHAR(120)"
            defaultValue = " NOT NULL DEFAULT '$default'"
        }
    }
    prepareStatement("ALTER TABLE `$table` ADD IF NOT EXISTS `${name}` $type$defaultValue AFTER `id`;").executeUpdate()

}

/**
 * Create multiple tables with a single update.
 */
fun createTables(vararg names: String) {
    var temp = ""
    for (name: String in names)
        temp = "$temp CREATE TABLE IF NOT EXISTS `$name` ( `id` VARCHAR(64) NOT NULL, PRIMARY KEY (`id`));"
    prepareStatement(temp).executeUpdate()
}

/**
 * Create multiple tables with a single update.
 */
fun createTables(names: List<String>) {
    val ps = ds.connection.createStatement()
    for (name: String in names)
        ps.addBatch("CREATE TABLE IF NOT EXISTS `$name` ( `id` VARCHAR(64) NOT NULL, PRIMARY KEY (`id`));")
    ps.executeBatch()
}

fun getColumnCount(table: String): Int {
    val stat =
        prepareStatement("SELECT Count(*) FROM INFORMATION_SCHEMA.Columns where TABLE_NAME = '$table';").executeQuery()
    return if (stat.next())
        stat.getInt("Count(*)")
    else
        0
}

fun getColumnNames(table: String): List<String> {
    val l = ArrayList<String>()
    val ps =
        prepareStatement("SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_NAME`= '$table';").executeQuery()
    while (ps.next())
        l.add(ps.getString("COLUMN_NAME"))
    return l
}


fun addRow(table: String, id: String) {
    val ids = StringBuilder()
    for (i in 0 until getColumnCount(table) - 1) {
        ids.append(", DEFAULT")
    }

    prepareStatement("INSERT IGNORE INTO `$table` values ('$id'$ids)").executeUpdate()

}

fun addRows(table: String, vararg names: String) {
    names.forEach { addRow(table, it) }
}


fun isInTable(table: String, value: String): Boolean {
    return prepareStatement("SELECT * FROM `$table` WHERE id = '$value';").executeQuery().next()
}

