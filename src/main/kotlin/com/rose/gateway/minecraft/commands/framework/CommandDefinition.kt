package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.shared.trie.Trie

data class CommandDefinition(
    val name: String,
    val documentation: String,
    val executors: List<CommandExecutor>,
    val subcommands: Map<String, Command>,
    val subcommandNames: Trie
)
