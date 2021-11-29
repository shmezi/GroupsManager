package me.alexirving

import me.clip.placeholderapi.PlaceholderAPI
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit.*
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin

fun log(type: LogType, toLog: String) {
    var prefix = ""
    when (type) {
        LogType.ERROR -> {
            prefix = "$YELLOW[$RED$type$YELLOW]$RESET "
        }
        LogType.INFO -> {
            prefix = "$CYAN[$PURPLE$type$CYAN]$RESET "
        }
        LogType.LOG -> {
            "$CYAN[$type]$RESET"
        }
    }
    getLogger().info("$prefix $toLog")
}

fun formatMessage(player: Player, message: String): String {
    return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, message))
}

fun replacePlaceHolders(message: String, player: Player): String {
    TODO("Gonna do some epic regex grouping and a simple hashmap.")
}

enum class LogType {
    ERROR,
    INFO,
    LOG
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


/**
 * Copied straight from the wiki
 */
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

/**
 * Copied straight from the wiki
 */
fun setupPermissions(): Boolean {
    val rsp = getServer().servicesManager.getRegistration(
        Permission::class.java
    )
    perms = rsp.provider
    return perms != null
}

fun registerEvents(plugin: JavaPlugin, vararg listeners: Listener) {
    val server = getPluginManager()
    for (listener: Listener in listeners)
        server.registerEvents(listener, plugin)

}



