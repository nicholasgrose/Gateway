package com.rose.gateway.minecraft.commands.framework.data.context

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * The context of an action for a Bukkit command
 *
 * @property sender The sender of the action
 * @property command The command
 * @property args The args that exist for the action
 * @constructor Create a Bukkit context
 */
sealed class BukkitContext(
    val sender: CommandSender,
    val command: Command,
    val args: Array<String>,
) {
    /**
     * A Bukkit command tab completion context
     *
     * @property alias The alias for the command
     * @constructor Create a Bukkit tab completion context
     *
     * @param sender The sender of the tab complete request
     * @param command The command that is completing
     * @param args The args that exist at the moment of completion
     */
    class TabComplete(
        sender: CommandSender,
        command: Command,
        val alias: String,
        args: Array<String>,
    ) : BukkitContext(sender, command, args)

    /**
     * A Bukkit command execution context
     *
     * @property label The label for the command
     * @constructor Create a Bukkit command execution context
     *
     * @param sender The sender of the command execution
     * @param command The command that is executing
     * @param args The args that were passed in for execution
     */
    class CommandExecute(
        sender: CommandSender,
        command: Command,
        val label: String,
        args: Array<String>,
    ) : BukkitContext(sender, command, args)
}
