package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.shared.collections.trie.Trie

/**
 * Definition of a command
 *
 * @property name The command's name
 * @property baseCommand The base command this command branches from
 * @property executors The possible executors for this command in order of priority
 * @property subcommands The commands that may branch off of this command
 * @property subcommandNames The names of the subcommands
 * @constructor Create a command definition
 */
data class CommandDefinition(
    val name: String,
    val baseCommand: String,
    val executors: List<CommandExecutor<*>>,
    val subcommands: Map<String, Command>,
    val subcommandNames: Trie
)
