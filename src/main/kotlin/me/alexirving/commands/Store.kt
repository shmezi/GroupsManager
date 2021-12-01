package me.alexirving.commands

import me.alexirving.econ
import me.alexirving.groups.Manager
import me.alexirving.groups.Track
import me.alexirving.groups.getDefFormat
import me.alexirving.groups.getTrackFromName
import me.alexirving.utils.LogType
import me.alexirving.utils.formatMessage
import me.alexirving.utils.log
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
            if (args.isEmpty()) {

                val message = StringBuilder("&b&l${getDefFormat("SHOPS_PREFIX")}")
                for (track: Track in Manager.tracks) {
                    var i = ""
                    var priceVal = ""
                    val a = (track.getNextGroup(
                        track.getPlayerGroup(player)
                    ))
                    if (a == null)
                        i = getDefFormat("HIGHEST")
                    else {
                        i = a.name
                        priceVal = " for ${a.price}"
                    }
                    message.append("\n${track.name} - $i$priceVal")
                }
                player.sendMessage(formatMessage(player, message.toString()))
            } else {
                if (args.size >= 2) {
                    when (args[0].uppercase()) {
                        "BUY" -> {
                            val t = getTrackFromName(args[1])
                            if (t != null) {
                                val g = t.getNextGroup(t.getPlayerGroup(player))
                                if (g != null)
                                    if (econ!!.has(player, g.price.toDouble())) {
                                        econ!!.withdrawPlayer(player, g.price.toDouble())
                                    } else {
                                        player.sendMessage(getDefFormat("BALANCE"))
                                    }
                                else {
                                    player.sendMessage(getDefFormat("BUY_TOP"))
                                }
                            } else
                                player.sendMessage(getDefFormat("NO_EXISTS").replace("%thing%", "Track"))
                        }
                        "INFO" -> {
                            TODO("To be added at a later date!")
                        }
                        else -> {
                            player.sendMessage(getDefFormat("IARGS"))
                        }
                    }
                } else {
                    player.sendMessage(getDefFormat("ARGS"))
                }
            }
        } else {
            log(LogType.ERROR, "Command can only be run as a player!")
        }

        return true; }
}