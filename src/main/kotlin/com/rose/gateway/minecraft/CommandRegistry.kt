package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.configuration.PluginConfiguration
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

    private val botCommands = BotCommands()
    private val configCommands = ConfigCommands()
    private val configMonitoringCommands = ConfigMonitoringRunner()
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
                        ConfigValueArg("VALUE", 0, config, configCompleter::configValueCompletion)
                    ) { context ->
                        configCommands.setConfiguration(context)
                    }
                }

                subcommand("add") {
                    runner(
                        StringArg("CONFIG_PATH", configCompleter::collectionConfigNameCompletion),
                        ConfigListValueArg("VALUE", 0, config),
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
                            config,
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
