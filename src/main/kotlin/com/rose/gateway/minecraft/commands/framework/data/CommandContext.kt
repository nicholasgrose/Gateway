package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import org.bukkit.command.CommandSender

data class CommandContext<A : RunnerArguments<A>>(
    val definition: CommandDefinition,
    val sender: CommandSender,
    val command: org.bukkit.command.Command,
    val label: String,
    val arguments: A
)
