package me.alexirving

import me.alexirving.commands.Debug
import me.alexirving.commands.Manage
import me.alexirving.commands.Reload
import me.alexirving.commands.Store
import me.alexirving.events.PlayerJoin
import me.alexirving.events.PlayerLeave
import me.alexirving.groups.*
import me.alexirving.utils.*
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

var econ: Economy? = null
var perms: Permission? = null

class GroupManager : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()
        if (!File(dataFolder, "lang.properties").exists())
            saveResource("lang.properties", false)
        getCommand("store").setExecutor(Store())
        getCommand("debug").setExecutor(Debug())
        getCommand("manage").setExecutor(Manage())
        getCommand("gmreload").setExecutor(Reload(this))
        val sql = config.getConfigurationSection("Sql")
        initDb(
            sql.getString("Host"),
            sql.getInt("Port"),
            sql.getString("Database"),
            sql.getString("Username"),
            sql.getString("Password")
        )
        loadLang(File(dataFolder, "lang.properties"))
        createTables("GM_TRACKS", "GM_PLAYTIME")
        log(LogType.INFO, "Started up!")

        reloadTracks(config.getConfigurationSection("Tracks"))
        setupEconomy()
        setupPermissions()
        registerEvents(this, PlayerLeave(), PlayerJoin())
        if (server.pluginManager.getPlugin("PlaceholderAPI") != null)
            Manager.papi = true
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, Runnable {
            for (player: Player in Bukkit.getOnlinePlayers()) {
                for (track: Track in Manager.tracks) {
                    updatePlayTime(track, player)
                    Manager.players.put(player, 0)
                    val g = track.getNextGroup(track.getPlayerGroup(player))
                    if (g != null)
                        if (getTime(track, player) >= g.time) {
                            track.promote(player)
                            subtractFromTime(track, player, g.time)
                        }
                }
            }
        }, 0L, config.getLong("Refresh"))
    }
}

