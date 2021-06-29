package com.rose.gateway.minecraft.commands.framework

data class CommandDefinition(
    val name: String,
    val usage: String,
    val checker: Checker,
    val runner: ((CommandContext) -> Boolean),
    val subcommands: Map<String, Command>
)
