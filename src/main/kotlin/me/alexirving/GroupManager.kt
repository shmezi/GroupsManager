package me.alexirving

import me.alexirving.commands.Debug
import me.alexirving.commands.Store
import me.alexirving.events.PlayerJoin
import me.alexirving.events.PlayerLeave
import me.alexirving.groups.Manager
import me.alexirving.groups.reloadTracks
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.plugin.java.JavaPlugin

var econ: Economy? = null
var perms: Permission? = null

class GroupManager : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()
        getCommand("store").setExecutor(Store())
        getCommand("debug").setExecutor(Debug())
        val sql = config.getConfigurationSection("Sql")
        initDb(
            sql.getString("Host"),
            sql.getInt("Port"),
            sql.getString("Database"),
            sql.getString("Username"),
            sql.getString("Password")
        )
        createTables("GM_TRACKS", "GM_PLAYTIME")
        log(LogType.INFO, "Started up!")

        reloadTracks(config.getConfigurationSection("Tracks"))
        setupEconomy()
        setupPermissions()
        registerEvents(this, PlayerLeave(), PlayerJoin())
        if (server.pluginManager.getPlugin("PlaceholderAPI") != null)
            Manager.papi = true


    }
}

