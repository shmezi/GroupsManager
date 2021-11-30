package me.alexirving

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.entity.Player
import java.sql.PreparedStatement
import java.sql.SQLException


var config = HikariConfig()
var ds: HikariDataSource = HikariDataSource()

/**
 * Initialize the database pool.
 * @param host The host IP of the database.
 * @param port The port of the database.
 * @param database The database name of the database.
 * @param username The Username for the database.
 * @param password The Password for the database.
 */
fun initDb(host: String, port: Int, database: String, username: String, password: String) {
    config.jdbcUrl = "jdbc:mysql://${host}:${port}/${database}"
    config.username = username
    config.password = password
    ds = HikariDataSource(config)
    ds.connectionInitSql
}


/**
 * Prepare an SQL statement with the database pool
 * @param statement The statement to prepare.
 */
fun prepareStatement(statement: String): PreparedStatement {
    val ps = ds.getConnection().prepareStatement(statement)
    ds.close()
    return ps
}


/**
 * Set a value in the database
 * @param table The table to set this value in.
 * @param key The key (column name) to set.
 * @param value The value to set.
 * @param player The player to set this value for.
 */
fun setValue(table: String, player: Player, key: String, value: String) {
    prepareStatement("UPDATE $table SET $key = '${value}' WHERE uuid = '${player.uniqueId}';")
}

/**
 * Get a player's group on a track.
 * @param table The table to get this value from.
 * @param player The player to get the current group from.
 * @param key The key (column name of the value to get.)
 * @return Returns the current value of a player in the table.
 */
fun getValue(table: String, player: Player, key: String): String {
    val prep = prepareStatement("SELECT * FROM $table WHERE uuid = '${player.uniqueId}';").executeQuery()
    return if (prep.next())
        prep.getString(key)
    else "null"
}

/**
 * Adds a column to the desired table
 * @param table The table to add to.
 * @param name The column name to add.
 */
fun addColumn(table: String, name: String, default: String) {
    prepareStatement(
        "ALTER TABLE $table ADD IF NOT EXISTS `${name}` VARCHAR(120) NOT NULL DEFAULT '$default' AFTER `uuid`;"
    ).executeUpdate()
}

/**
 * Create a table / tables if it / they doesn't / don't already exist.
 * @param names The name of the table to create.
 */
fun createTables(vararg names: String) {
    for (name: String in names)
        prepareStatement("CREATE TABLE IF NOT EXISTS `$name` ( `uuid` VARCHAR(64) NOT NULL, PRIMARY KEY (`uuid`));").executeUpdate()
}


/**
 * Get the amount of columns in the database. Useful for adding a player to a database.
 */
fun getColumnCount(table: String): Int {
    val stat =
        prepareStatement("SELECT Count(*) FROM INFORMATION_SCHEMA.Columns where TABLE_NAME = '$table';").executeQuery()
    return if (stat.next())
        stat.getInt("Count(*)")
    else
        0
}

/**
 * Add a player to the database
 * @param player The player to be added to the database.
 */
fun addPlayerToTable(table: String, player: Player) {
    val ids = StringBuilder()
    for (i in 0 until getColumnCount(table) - 1) {
        ids.append(", DEFAULT")
    }
    prepareStatement("INSERT INTO $table values ('${player.uniqueId}'${ids});").executeUpdate()


}

/**
 * Checks if a player is in a table.
 * @param table The table to check if a player is in.
 * @param player The player to check if they are in the database
 */
fun isInTable(table: String, player: Player): Boolean {
    return try {
        prepareStatement(
            "SELECT * FROM $table WHERE uuid = '${player.uniqueId}';"
        ).executeQuery().next()
    } catch (e: SQLException) {
        e.printStackTrace()
        false
    }
}