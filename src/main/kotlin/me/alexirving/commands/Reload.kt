package me.alexirving.commands

import me.alexirving.groups.getDefFormat
import me.alexirving.groups.loadLang
import me.alexirving.groups.reloadTracks
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Reload(val plugin: JavaPlugin) : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        plugin.reloadConfig()
        reloadTracks(plugin.config.getConfigurationSection("Tracks"))
        loadLang(File(plugin.dataFolder, "lang.properties"))
        sender.sendMessage(getDefFormat("RELOAD"))
        return true
    }
}