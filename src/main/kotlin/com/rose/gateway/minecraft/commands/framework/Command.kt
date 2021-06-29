package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.GatewayPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class Command(val definition: CommandDefinition) : CommandExecutor, TabCompleter {
    companion object {
        fun subcommandRunner(context: CommandContext): Boolean {
            val subcommand = context.commandArguments[0] as String
            val childCommand = context.definition.subcommands[subcommand]
            val arguments = context.rawCommandArguments

            return if (childCommand == null) false
            else {
                childCommand.onCommand(
                    sender = context.sender,
                    command = context.command,
                    label = context.label,
                    args = arguments.subList(1, arguments.size).toTypedArray()
                )
                true
            }
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        label: String,
        args: Array<String>
    ): Boolean {
        val convertedArguments = definition.argumentParser.parseArguments(args)

        if (convertedArguments == null) sendArgumentErrorMessage(sender)
        else try {
            val success = definition.runner(
                CommandContext(
                    definition = definition,
                    sender = sender,
                    command = command,
                    label = label,
                    rawCommandArguments = args.toList(),
                    commandArguments = convertedArguments
                )
            )

            if (!success) sendArgumentErrorMessage(sender)
        } catch (e: Error) {
            sender.sendMessage(Component.text("Error: ${e.message}", TextColor.fromHexString("#FF0000")))
            sendArgumentErrorMessage(sender)
        }

        return true
    }

    private fun sendArgumentErrorMessage(sender: CommandSender) {
        val message = "Correct usage: ${definition.usage}"
        sender.sendMessage(message)
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: org.bukkit.command.Command,
        alias: String,
        args: Array<String>
    ): MutableList<String>? {
        if (definition.subcommands.isEmpty()) return null

        return if (args.size > 1) {
            val subcommand = definition.subcommands[args[0]]
            subcommand?.onTabComplete(sender, command, alias, args.sliceArray(1 until args.size))
        } else definition.subcommands.keys.toMutableList()
    }

    fun registerCommand() {
        GatewayPlugin.plugin.getCommand(definition.name)?.setExecutor(this)
    }
}
