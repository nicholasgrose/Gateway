package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import org.bukkit.command.CommandSender

data class CommandContext<A : CommandArgs<A>>(
    val sender: CommandSender,
    val bukkitCommand: org.bukkit.command.Command,
    val label: String,
    val args: A
)
