package me.alexirving.groups

import me.alexirving.formatMessage
import org.bukkit.Bukkit.getServer
import org.bukkit.entity.Player


/**
 * An action Object is an action that can be executed through the plugin like [Command][ActionType.COMMAND] and [Message][ActionType.MESSAGE].
 * @param type The Type of action.
 * @param contents A list of the contents of the action like for example the message to be sent.
 */
class Action(val type: ActionType, val contents: List<String>) {
    /**
     * Execute the action on a player!
     * @param player Player to execute this action on.
     */
    fun execute(player: Player) {
        when (type) {
            ActionType.COMMAND -> {
                for (content: String in contents) {
                    getServer().dispatchCommand(
                        getServer().consoleSender, formatMessage(player, content)
                    )
                }

            }
            ActionType.MESSAGE -> {
                for (content: String in contents) {
                    player.sendMessage(formatMessage(player, content))
                }


            }


        }
    }
}
