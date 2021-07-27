package com.rose.gateway.minecraft.commands.framework

import org.bukkit.command.CommandSender

data class TabCompletionContext(
    val sender: CommandSender,
    val command: org.bukkit.command.Command,
    val alias: String,
    val rawArguments: List<String>,
    val parsedArguments: List<*>,
    val commandDefinition: CommandDefinition
)
