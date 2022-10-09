package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import org.bukkit.command.CommandSender

/**
 * The context surrounding a command's execution
 *
 * @param A The type of the args in the context
 * @property definition The definition of the command being executed
 * @property sender The sender of the command from Bukkit
 * @property command The command being executed in Bukkit
 * @property label The label of the command from Bukkit
 * @property args The command's arguments
 * @constructor Create a command context
 */
data class CommandContext<A : CommandArgs<A>>(
    val definition: CommandDefinition,
    val sender: CommandSender,
    val command: org.bukkit.command.Command,
    val label: String,
    val args: A
)
