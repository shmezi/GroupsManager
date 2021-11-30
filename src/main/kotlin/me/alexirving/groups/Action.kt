package me.alexirving.groups

import org.bukkit.entity.Player


/**
 * An action Object is an action that can be executed through the plugin like [Command][ActionType.COMMAND] and [Message][ActionType.MESSAGE].
 * @param type The Type of action.
 * @param contents A list of the contents of the action like for example the message to be sent.
 */

abstract class Action(val type: ActionType, val content: String) {
    /**
     * Execute the action on a player!
     * @param player Player to execute this action on.
     */
    abstract fun execute(player: Player)
}
