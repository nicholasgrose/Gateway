package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.shared.collections.trie.Trie

data class CommandDefinition(
    val name: String,
    val baseCommand: String,
    val executors: List<CommandExecutor<*>>,
    val subcommands: Map<String, Command>,
    val subcommandNames: Trie
)
