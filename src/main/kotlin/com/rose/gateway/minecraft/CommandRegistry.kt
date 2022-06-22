package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.commands.arguments.ConfigNameArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigValueArgs
import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.converters.ConfigListValueArg
import com.rose.gateway.minecraft.commands.converters.ConfigValueArg
import com.rose.gateway.minecraft.commands.converters.StringArg
import com.rose.gateway.minecraft.commands.framework.MinecraftCommandsBuilder.Companion.minecraftCommands
import com.rose.gateway.minecraft.commands.runners.BotCommands
import com.rose.gateway.minecraft.commands.runners.ConfigCommands
import com.rose.gateway.minecraft.commands.runners.ConfigMonitoringRunner
import com.rose.gateway.minecraft.commands.runners.GeneralCommands
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CommandRegistry : KoinComponent {
    private val plugin: GatewayPlugin by inject()
    private val config: PluginConfiguration by inject()

    private val configCompleter = ConfigCompleter()

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
                // TODO
                context.sender.sendMessage("Not yet implemented.")
                true
            }
        }

        command("discord") {
            runner { context ->
                // TODO
                context.sender.sendMessage("Not yet implemented.")
                true
            }
        }

        command("gateway") {
            subcommand("bot") {
                subcommand("restart") {
                    runner { context -> BotCommands.restartBot(context) }
                }

                subcommand("status") {
                    runner { context -> BotCommands.botStatus(context) }
                }
            }

            subcommand("config") {
                subcommand("set") {
                    runner(
                        StringArg("CONFIG_PATH", configCompleter::configNameCompletion),
                        ConfigValueArg("VALUE", 0, config, configCompleter::configValueCompletion)
                    ) { context ->
                        ConfigCommands.setConfiguration(context)
                    }
                }

                subcommand("add") {
                    runner(
                        StringArg("CONFIG_PATH", configCompleter::collectionConfigNameCompletion),
                        ConfigListValueArg("VALUE", 0, config),
                        allowVariableNumberOfArguments = true
                    ) { context ->
                        ConfigCommands.addConfiguration(context)
                    }
                }

                subcommand("remove") {
                    runner(
                        StringArg("CONFIG_PATH", configCompleter::collectionConfigNameCompletion),
                        ConfigListValueArg(
                            "VALUE",
                            0,
                            config,
                            configCompleter::collectionConfigValueCompletion
                        ),
                        allowVariableNumberOfArguments = true
                    ) { context ->
                        ConfigCommands.removeConfiguration(context)
                    }
                }

                subcommand("help") {
                    runner(::ConfigValueArgs) { context ->
                        ConfigMonitoringRunner.sendConfigurationHelp(context)
                    }

                    runner(::ConfigNameArgs) { context ->
                        ConfigMonitoringRunner.sendConfigurationSearchHelp(context)
                    }

                    runner { context ->
                        ConfigMonitoringRunner.sendAllConfigurationHelp(context.sender)
                    }
                }

                subcommand("reload") {
                    runner { context ->
                        ConfigMonitoringRunner.reloadConfig(context)
                    }
                }

                subcommand("save") {
                    runner { context ->
                        ConfigMonitoringRunner.saveConfig(context)
                    }
                }

                subcommand("status") {
                    runner { context ->
                        ConfigMonitoringRunner.sendConfigurationStatus(context.sender)
                    }
                }
            }
        }
    }
}
