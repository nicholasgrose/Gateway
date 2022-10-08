package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import org.bukkit.command.CommandSender

data class TabCompletionContext<A : CommandArgs<A>>(
    val sender: CommandSender,
    val command: org.bukkit.command.Command,
    val alias: String,
    val arguments: A,
    val commandDefinition: CommandDefinition
)
