package me.alexirving.commands

import me.alexirving.LogType
import me.alexirving.formatMessage
import me.alexirving.groups.getTrackFromName
import me.alexirving.log
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Store : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (sender is Player) {
            val player = sender as Player;
            if (args.size >= 2) {
                if (getTrackFromName(args[0]) == null)
                    player.sendMessage(formatMessage(player, "That track ain't existing!"))
                else {
                    if (getTrackFromName(args[0])!!.promote(player))
                        player.sendMessage(formatMessage(player, "Promoting your ass!"))
                    else
                        player.sendMessage(formatMessage(player, "U too good I think or too bad!"))
                }
            }
        } else {
            log(
                LogType.ERROR,
                "&cCommand must be sent as a player for some reason in this case even tho it doesn't make sense."
            )
        }
        return true; }
}