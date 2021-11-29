package me.alexirving

import me.alexirving.commands.Manage
import me.alexirving.events.PlayerJoin
import me.alexirving.events.PlayerLeave
import me.alexirving.groups.Manager
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.plugin.java.JavaPlugin

var econ: Economy? = null
var perms: Permission? = null

class GroupManager : JavaPlugin() {

    override fun onEnable() {
        getCommand("manager").executor = Manage()
        logger.info("Devnics group manager!")
        setupEconomy()
        setupPermissions()
        registerEvents(this, PlayerLeave(), PlayerJoin())
        if (server.pluginManager.getPlugin("") != null)
            Manager.papi = true
    }
}

