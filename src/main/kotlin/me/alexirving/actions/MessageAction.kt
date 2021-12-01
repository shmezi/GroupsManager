package me.alexirving.actions

import me.alexirving.utils.formatMessage
import org.bukkit.entity.Player

class MessageAction(val message: String) : Action(ActionType.MESSAGE, message) {
    override fun execute(player: Player) {
        player.sendMessage(formatMessage(player, message))
    }
}