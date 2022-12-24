package com.rose.gateway.minecraft.commands.framework.data.context

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

public sealed class BukkitContext(
    val sender: CommandSender,
    val command: Command,
    val args: Array<String>
) {
    public class TabComplete(
        sender: CommandSender,
        command: Command,
        val alias: String,
        args: Array<String>
    ) : BukkitContext(sender, command, args)

    public class CommandExecute(
        sender: CommandSender,
        command: Command,
        val label: String,
        args: Array<String>
    ) : BukkitContext(sender, command, args)
}
