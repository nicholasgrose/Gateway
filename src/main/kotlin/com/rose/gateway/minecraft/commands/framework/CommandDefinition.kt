package com.rose.gateway.minecraft.commands.framework

data class CommandDefinition(
    val name: String,
    val documentation: String,
    val executors: List<CommandExecutor>,
    val subcommands: Map<String, Command>
)
