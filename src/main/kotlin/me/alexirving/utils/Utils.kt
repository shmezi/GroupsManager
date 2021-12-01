package me.alexirving.utils

import me.alexirving.*
import me.alexirving.actions.Action
import me.alexirving.actions.ActionType
import me.alexirving.actions.CommandAction
import me.alexirving.actions.MessageAction
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
import java.util.regex.Pattern

/**
 * Simplifies the task of logging while making sure all logging looks the same.
 * @param type The type of log to log.
 * @param toLog The message to log.
 */
fun log(type: LogType, toLog: String) {
    val prefix = when (type) {
        LogType.ERROR -> {
            "$YELLOW[$RED$type$YELLOW]$RESET"
        }
        LogType.INFO -> {
            "$CYAN[$PURPLE$type$CYAN]$RESET"
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

fun compileAction(action: String): Action? {
    val validate =
        Pattern.compile("(^(?=COMMAND (PLAYER|CONSOLE) (..*)$))|(^(?=MESSAGE (..*)$))")
    val matcher = validate.matcher(action)
    return if (matcher.find()) {
        val p =
            Pattern.compile("^(?<Prefix>COMMAND|MESSAGE) (?<Action>PLAYER|CONSOLE)? ?(?<Content>..*)")
        val m2 = p.matcher(action)
        m2.find()
        when (ActionType.valueOf(m2.group("Prefix"))) {
            ActionType.COMMAND -> {
                CommandAction(m2.group("Content"), m2.group("Action").equals("CONSOLE"))
            }
            ActionType.MESSAGE -> {
                MessageAction(m2.group("Content"))
            }
        }
    } else {
        null
    }
}




