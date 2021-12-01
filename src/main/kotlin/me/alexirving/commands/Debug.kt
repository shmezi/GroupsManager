package me.alexirving.commands

import me.alexirving.actions.Action
import me.alexirving.getValue
import me.alexirving.groups.Group
import me.alexirving.groups.Manager
import me.alexirving.groups.Track
import me.alexirving.setValue
import me.alexirving.utils.LogType
import me.alexirving.utils.log
import org.bukkit.Bukkit
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
        if (args.isNotEmpty())
            when (args[0].lowercase()) {
                "dbget" -> {
                    if (args.size > 3) {
                        sender.sendMessage(getValue(args[1], Bukkit.getPlayer(args[2]), args[3]))
                    } else {
                        sender.sendMessage("Usage: /debug dbget <table> <player> <column>")
                    }
                }
                "dbset" -> {
                    if (args.size > 4) {
                        sender.sendMessage("Set value!")
                        setValue(args[1], Bukkit.getPlayer(args[2]), args[3], args[4])
                        sender.sendMessage(
                            "After a query found: ${
                                getValue(
                                    args[1],
                                    Bukkit.getPlayer(args[2]),
                                    args[3]
                                )
                            }"
                        )
                    } else {
                        sender.sendMessage("Usage: /debug dbset <table> <player> <column> <value>")
                    }
                }

                else -> {
                    log(LogType.INFO, "Sending debug info!")
                    log(LogType.INFO, "Tracks:")
                    for (track: Track in Manager.tracks) {
                        log(LogType.LOG, "Registered a track called: ${track.name}")
                        log(LogType.INFO, "Groups:")
                        for (group: Group in track.groups) {
                            log(LogType.LOG, "Registered a group called: ${group.name}")
                            log(LogType.INFO, "Actions:")
                            if (group.promoteActions != null)
                                for (action: Action in group.promoteActions)
                                    log(
                                        LogType.LOG,
                                        "Registered an action. Type: ${action.type} With the content: ${action.content}"
                                    )
                            if (group.demoteActions != null)
                                for (action: Action in group.demoteActions)
                                    log(
                                        LogType.LOG,
                                        "Registered an action. Type: ${action.type} With the content: ${action.content}"
                                    )
                        }
                    }
                }
            }
        else
            sender.sendMessage("Please add some args!")
        return true
    }
}