package com.rose.gateway.minecraft.commands.framework

import org.bukkit.command.CommandSender

data class CommandContext(
    val definition: CommandDefinition,
    val sender: CommandSender,
    val command: org.bukkit.command.Command,
    val label: String,
    val rawCommandArguments: List<String>,
    val commandArguments: List<*>
)
