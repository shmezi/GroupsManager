package me.alexirving.commands

import me.alexirving.groups.getDefFormat
import me.alexirving.groups.getTrackFromName
import me.alexirving.isInTable
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.regex.Pattern

class Manage : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (checkTrack(args, sender)) {
            val track = getTrackFromName(args[1])!!
            val player = Bukkit.getPlayer(args[0])
            when (args[2].uppercase()) {
                "PROMOTE" -> {
                    if (track.promote(player))
                        sender.sendMessage(getDefFormat("PROMOTED"))
                    else
                        sender.sendMessage(getDefFormat("PROMOTE_TOP"))
                }
                "DEMOTE" -> {
                    if (track.demote(player))
                        sender.sendMessage(getDefFormat("DEMOTED"))
                    else
                        sender.sendMessage(getDefFormat("DEMOTE_BOTTOM"))
                }
                "SET" -> {
                    if (args.size > 3) {
                        val g = track.getGroupFromName(args[3])
                        if (track.getGroupFromName(args[3]) != null) {
                            track.setPlayerGroup(g!!, player)
                            sender.sendMessage(getDefFormat("GROUP_SET"))
                        } else
                            sender.sendMessage(getDefFormat("NO_EXIST").replace("%thing%", "Group"))
                    } else {
                        sender.sendMessage(getDefFormat("ARGS"))
                    }
                }
                else -> {
                    sender.sendMessage(getDefFormat("IARGS"))
                }
            }
        }
        return true
    }

    private fun checkTrack(args: Array<String>, sender: CommandSender): Boolean {
        if (args.size >= 3) {
            val a = StringBuilder()
            var f = false
            for (i: String in args) {
                if (!f) {
                    a.append(i.uppercase())
                    f = true
                } else
                    a.append(" ${i.uppercase()}")
            }
            if (Pattern.compile("(..*) (..*) (PROMOTE$|DEMOTE$|(?=SET (..*)$))").matcher(a).find()) {
                if (getTrackFromName(args[1]) != null) {
                    val player = Bukkit.getOfflinePlayer(args[0]).player
                    if (isInTable("GM_TRACKS", player)) {
                        return true
                    } else {
                        sender.sendMessage(getDefFormat("NOT_IN_DB"))
                    }
                } else {
                    sender.sendMessage(getDefFormat("NO_EXIST").replace("%thing%", "Track"))
                }
            } else {
                sender.sendMessage(getDefFormat("IARGS"))
            }
        } else {
            sender.sendMessage(getDefFormat("ARGS"))
        }
        return false
    }
}