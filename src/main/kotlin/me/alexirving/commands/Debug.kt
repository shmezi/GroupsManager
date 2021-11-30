package me.alexirving.commands

import me.alexirving.LogType
import me.alexirving.groups.Group
import me.alexirving.groups.Manager
import me.alexirving.groups.Track
import me.alexirving.log
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class Debug : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        for (track: Track in Manager.tracks) {
            log(LogType.LOG, "Registered a track called: ${track.name}")
            for (group: Group in track.groups) {
                log(LogType.LOG, "Registered a group called: ${group.name}")
            }
        }
        return true
    }
}