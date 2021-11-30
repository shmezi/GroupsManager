package me.alexirving.groups

import me.alexirving.getValue
import me.alexirving.noLowerThenZero
import me.alexirving.setValue
import org.bukkit.entity.Player

/**
 * A track is a list of groups that a player can be promoted and demoted from.
 * @param name Name of the track
 * @param groups List of the groups.
 */
class Track(val name: String, val groups: ArrayList<Group>) {
    /**
     * Promote the player on the track.
     * @param player Player to be promoted
     */
    fun promote(player: Player): Boolean {
        val group = getNextGroup(getPlayerGroup(player))
        if (group == null)
            return false
        else
            setPlayerGroup(group, player)
        group.runPromotionActions(player)
        return true
    }

    /**
     * Demote the player on the track.
     * @param player The player to be demoted.
     */
    fun demote(player: Player): Boolean {
        val previous = getPlayerGroup(player)
        val group = getNextGroup(previous)
        if (group == null)
            return false
        else
            setPlayerGroup(group, player)
        previous.runDemotionActions(player)
        group.runPromotionActions(player)

        return true
    }

    /**
     * Set a player's group.
     */
    fun setPlayerGroup(group: Group, player: Player) {
        setValue("GM_TRACKS", player, name, group.name)
    }

    /**
     * Get the current group a player is in.
     */
    fun getPlayerGroup(player: Player): Group {
        val v = getValue("GM_TRACKS", player, name)
        return getGroupFromName(v)!!

    }

    /**
     * Gets a group from a string.
     */
    fun getGroupFromName(name: String): Group? {
        val fg = groups.filter { it.name == name }
        return if (fg.isEmpty())
            null
        else
            fg[0]
    }

    /**
     * Get the next group in the track
     */
    fun getNextGroup(group: Group): Group? {
        return if (groups.size - 1 < (groups.indexOf(group) + 1))
            null
        else
            groups[groups.indexOf(group) + 1]
    }

    /**
     * Get the previous group in the track
     */
    fun getPreviousGroup(group: Group): Group? {
        return if (noLowerThenZero(groups.size - 1) <= (groups.indexOf(group) - 1))
            null
        else
            groups[groups.indexOf(group) + 1]
    }


}