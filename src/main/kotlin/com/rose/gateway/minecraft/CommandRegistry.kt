package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.commands.BotCommands
import com.rose.gateway.minecraft.commands.ConfigCommands
import com.rose.gateway.minecraft.commands.GeneralCommands
import com.rose.gateway.minecraft.commands.framework.MinecraftCommandsBuilder.Companion.minecraftCommands
import com.rose.gateway.minecraft.commands.framework.converters.IntArg
import com.rose.gateway.minecraft.commands.framework.converters.StringArg

class CommandRegistry(val plugin: GatewayPlugin) {
    private val botCommands = BotCommands(plugin)
    private val configCommands = ConfigCommands(plugin.configuration)

    fun registerCommands() {
        commands.registerCommands()
    }

    private val commands = minecraftCommands(plugin) {
        command("discord") {
            runner { context ->
                GeneralCommands.discordHelp(context)
            }
        }

        command("gateway") {
            subcommand("bot") {
                subcommand("restart") {
                    runner { context -> botCommands.restartBot(context) }
                }

                subcommand("status") {
                    runner { context -> botCommands.botStatus(context) }
                }
            }

            subcommand("config") {
                subcommand("set") {
                    runner(
                        StringArg("CONFIGURATION_PATH", configCommands::configCompletion),
                        IntArg("VALUE")
                    ) { context ->
                        configCommands.setConfiguration(context)
                    }

                    runner(
                        StringArg("CONFIGURATION_PATH", configCommands::configCompletion),
                        StringArg("VALUE")
                    ) { context ->
                        configCommands.setConfiguration(context)
                    }
                }

                subcommand("add") {
                    runner(
                        StringArg("CONFIGURATION_PATH", configCommands::configCompletion),
                        StringArg("VALUE"),
                        allowVariableNumberOfArguments = true
                    ) { context ->
                        configCommands.addConfiguration(context)
                    }
                }

                subcommand("remove") {
                    runner(
                        StringArg("CONFIGURATION_PATH", configCommands::configCompletion),
                        StringArg("VALUE"),
                        allowVariableNumberOfArguments = true
                    ) { context ->
                        configCommands.removeConfiguration(context)
                    }
                }

                subcommand("help") {
                    runner(StringArg("CONFIGURATION_PATH", configCommands::configCompletion)) { context ->
                        val configuration = context.commandArguments[0] as String
                        configCommands.sendConfigurationHelp(context.sender, configuration)
                    }

                    runner { context ->
                        configCommands.sendConfigurationHelp(context.sender, "")
                    }
                }
            }
        }
    }
}
