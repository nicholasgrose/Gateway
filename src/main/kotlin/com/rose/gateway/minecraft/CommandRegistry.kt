package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.commands.BotCommands
import com.rose.gateway.minecraft.commands.ConfigCommands
import com.rose.gateway.minecraft.commands.GeneralCommands
import com.rose.gateway.minecraft.commands.framework.MinecraftCommandsBuilder.Companion.minecraftCommands
import com.rose.gateway.minecraft.commands.framework.converters.ConfigListValueArg
import com.rose.gateway.minecraft.commands.framework.converters.ConfigValueArg
import com.rose.gateway.minecraft.commands.framework.converters.StringArg

class CommandRegistry(val plugin: GatewayPlugin) {
    private val botCommands = BotCommands(plugin)
    private val configCommands = ConfigCommands(plugin)

    fun registerCommands() {
        commands.registerCommands()
    }

    private val commands = minecraftCommands(plugin) {
        command("discord-help") {
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
                        StringArg("CONFIG_PATH", configCommands::configCompletion),
                        ConfigValueArg("VALUE", 0, plugin.configuration, configCommands::configValueCompletion)
                    ) { context ->
                        configCommands.setConfiguration(context)
                    }
                }

                subcommand("add") {
                    runner(
                        StringArg("CONFIG_PATH", configCommands::collectionConfigNameCompletion),
                        ConfigListValueArg("VALUE", 0, plugin.configuration),
                        allowVariableNumberOfArguments = true
                    ) { context ->
                        configCommands.addConfiguration(context)
                    }
                }

                subcommand("remove") {
                    runner(
                        StringArg("CONFIG_PATH", configCommands::collectionConfigNameCompletion),
                        ConfigListValueArg(
                            "VALUE",
                            0,
                            plugin.configuration,
                            configCommands::collectionConfigValueCompletion
                        ),
                        allowVariableNumberOfArguments = true
                    ) { context ->
                        configCommands.removeConfiguration(context)
                    }
                }

                subcommand("help") {
                    runner(StringArg("CONFIG_PATH", configCommands::configCompletion)) { context ->
                        val configuration = context.commandArguments.first() as String
                        configCommands.sendConfigurationHelp(context.sender, configuration)
                    }

                    runner { context ->
                        configCommands.sendConfigurationHelp(context.sender, "")
                    }
                }

                subcommand("reload") {
                    runner { context ->
                        configCommands.reloadConfig(context)
                    }
                }

                subcommand("status") {
                    runner { context ->
                        configCommands.sendConfigurationStatus(context.sender)
                    }
                }
            }
        }
    }
}
