package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.converters.ConfigListValueArg
import com.rose.gateway.minecraft.commands.converters.ConfigValueArg
import com.rose.gateway.minecraft.commands.converters.StringArg
import com.rose.gateway.minecraft.commands.framework.MinecraftCommandsBuilder.Companion.minecraftCommands
import com.rose.gateway.minecraft.commands.runners.BotCommands
import com.rose.gateway.minecraft.commands.runners.ConfigCommands
import com.rose.gateway.minecraft.commands.runners.ConfigMonitoringRunner
import com.rose.gateway.minecraft.commands.runners.GeneralCommands

class CommandRegistry(val plugin: GatewayPlugin) {
    private val botCommands = BotCommands(plugin)
    private val configCommands = ConfigCommands(plugin)
    private val configMonitoringCommands = ConfigMonitoringRunner(plugin)
    private val configCompleter = ConfigCompleter(plugin)

    fun registerCommands() {
        commands.registerCommands()
    }

    private val commands = minecraftCommands(plugin) {
        command("discord-help") {
            runner { context ->
                GeneralCommands.discordHelp(context)
            }
        }

        command("discord-whisper") {
            runner { context ->
                TODO("Not yet implemented")
            }
        }

        command("discord") {
            runner { context ->
                TODO("Not yet implemented")
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
                        StringArg("CONFIG_PATH", configCompleter::configNameCompletion),
                        ConfigValueArg("VALUE", 0, plugin.configuration, configCompleter::configValueCompletion)
                    ) { context ->
                        configCommands.setConfiguration(context)
                    }
                }

                subcommand("add") {
                    runner(
                        StringArg("CONFIG_PATH", configCompleter::collectionConfigNameCompletion),
                        ConfigListValueArg("VALUE", 0, plugin.configuration),
                        allowVariableNumberOfArguments = true
                    ) { context ->
                        configCommands.addConfiguration(context)
                    }
                }

                subcommand("remove") {
                    runner(
                        StringArg("CONFIG_PATH", configCompleter::collectionConfigNameCompletion),
                        ConfigListValueArg(
                            "VALUE",
                            0,
                            plugin.configuration,
                            configCompleter::collectionConfigValueCompletion
                        ),
                        allowVariableNumberOfArguments = true
                    ) { context ->
                        configCommands.removeConfiguration(context)
                    }
                }

                subcommand("help") {
                    runner(StringArg("CONFIG_PATH", configCompleter::configNameCompletion)) { context ->
                        val configuration = context.commandArguments.first() as String
                        configMonitoringCommands.sendConfigurationHelp(context.sender, configuration)
                    }

                    runner { context ->
                        configMonitoringCommands.sendConfigurationHelp(context.sender, "")
                    }
                }

                subcommand("reload") {
                    runner { context ->
                        configMonitoringCommands.reloadConfig(context)
                    }
                }

                subcommand("status") {
                    runner { context ->
                        configMonitoringCommands.sendConfigurationStatus(context.sender)
                    }
                }
            }
        }
    }
}
