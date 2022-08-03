package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import org.bukkit.command.CommandSender

data class TabCompletionContext<A : RunnerArguments<A>>(
    val sender: CommandSender,
    val command: org.bukkit.command.Command,
    val alias: String,
    val arguments: A,
    val commandDefinition: CommandDefinition
)
