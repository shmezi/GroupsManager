package me.alexirving

import me.clip.placeholderapi.PlaceholderAPI
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit.getLogger
import org.bukkit.Bukkit.getServer
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin

/**
 * Simplifies the task of logging while making sure all logging looks the same.
 * @param type The type of log to log.
 * @param toLog The message to log.
 */
fun log(type: LogType, toLog: String) {
    var prefix = ""
    when (type) {
        LogType.ERROR -> {
            prefix = "$YELLOW[$RED$type$YELLOW]$RESET"
        }
        LogType.INFO -> {
            prefix = "$CYAN[$PURPLE$type$CYAN]$RESET"
        }
        LogType.LOG -> {
            "$CYAN[$type]$RESET"
        }
    }
    getLogger().info("$prefix$WHITE: $toLog")
}

/**
 * The types of logging
 */
enum class LogType {
    /**
     * Use for logging errors like in a try catch statement.
     */
    ERROR,

    /**
     * Use for logging info like if the plugin has been enabled
     */
    INFO,

    /**
     * Use for logging actions like if a player joined or quit.
     */
    LOG
}

/**
 * Format a message with placeholders and color.
 * @param player
 */
fun formatMessage(player: Player, message: String): String {
    return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, message))
}

fun replacePlaceHolders(message: String, player: Player): String {
    TODO("Gonna do some epic regex grouping and a simple hashmap.")
}

/**
 * Checks if a number is bellow 0, if it is returns 0, otherwise returns the number.
 * @param number The number to check if it's bellow 0
 */
fun noLowerThenZero(number: Int): Int {
    return if (number <= 0)
        0
    else
        number
}


fun setupEconomy(): Boolean {
    if (getServer().pluginManager.getPlugin("Vault") == null) {
        return false
    }
    val rsp: RegisteredServiceProvider<Economy> = getServer().getServicesManager().getRegistration(
        Economy::class.java
    ) ?: return false
    econ = rsp.provider
    return econ != null
}

fun setupPermissions(): Boolean {
    val rsp = getServer().servicesManager.getRegistration(
        Permission::class.java
    )
    perms = rsp.provider
    return perms != null
}

/**
 * Allows registering multiple [Listeners][Listener] in a single line of code.
 * @param plugin The instance of a plugin
 * @param listeners The Listeners to register.
 */
fun registerEvents(plugin: JavaPlugin, vararg listeners: Listener) {
    val server = plugin.server.pluginManager
    for (listener: Listener in listeners)
        server.registerEvents(listener, plugin)

}



