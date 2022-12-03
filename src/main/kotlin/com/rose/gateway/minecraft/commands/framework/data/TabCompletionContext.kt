package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import org.bukkit.command.CommandSender

data class TabCompletionContext<A : CommandArgs<A>>(
    val sender: CommandSender,
    val bukkitCommand: org.bukkit.command.Command,
    val command: Command,
    val alias: String,
    val args: A
)

data class BukkitContext(
    val sender: CommandSender,
    val bukkitCommand: org.bukkit.command.Command,
    val alias: String
)

data class FrameworkContext<T, A : CommandArgs<A>, P : ArgParser<T, A, P>>(
    val command: Command,
    val args: A,
    val completingArg: ArgParser<T, A, P>
)
