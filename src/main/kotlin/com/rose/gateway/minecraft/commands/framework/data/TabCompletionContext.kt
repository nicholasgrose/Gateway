package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import org.bukkit.command.CommandSender

/**
 * The context surrounding a command's tab completion
 *
 * @param A The type of the args in the context
 * @property sender The sender of the completion request from Bukkit
 * @property command The command being completed from Bukkit
 * @property alias The alias of the command being completed from Bukkit
 * @property args The command's arguments
 * @property definition The definition of the command being completed
 * @constructor Create a tab completion context
 */
data class TabCompletionContext<A : CommandArgs<A>>(
    val sender: CommandSender,
    val command: org.bukkit.command.Command,
    val alias: String,
    val args: A,
    val definition: CommandDefinition
)
