package me.alexirving.actions

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CommandAction(command: String, val asConsole: Boolean) : Action(ActionType.COMMAND, command) {
    override fun execute(player: Player) {
        if (asConsole)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), content)
        else
            player.performCommand(content)

    }
}