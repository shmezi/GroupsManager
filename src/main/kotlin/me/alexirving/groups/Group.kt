package me.alexirving.groups

import me.alexirving.actions.Action
import org.bukkit.entity.Player

/**
 * @param name The name of the Group
 * @param price The worth of the group (cost for a player in store to upgrade from it)
 * @param promoteActions The list of actions to run when a user purchases / is upgraded by an admin.
 * @param demoteActions The list of action to run when a user is demoted from this group.
 */
class Group(
    val name: String,
    val price: Int,
    val time: Long,
    val promoteActions: ArrayList<Action>?,
    val demoteActions: ArrayList<Action>?
) {
    /**
     * Run the actions for promoting a user.
     * @param player The player to be promoted
     */
    fun runPromotionActions(player: Player) {
        if (promoteActions != null)
            for (action: Action in promoteActions) {
                action.execute(player)
            }
    }

    /**
     * Run the actions for demoting a user.
     * @param player The player to be promoted
     */
    fun runDemotionActions(player: Player) {
        if (demoteActions != null)
            for (action: Action in demoteActions) {
                action.execute(player)
            }
    }


}