package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.commands.arguments.ConfigItemArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigNameArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigValueArgs
import com.rose.gateway.minecraft.commands.framework.MinecraftCommandsBuilder.Companion.minecraftCommands
import com.rose.gateway.minecraft.commands.runners.BotCommands
import com.rose.gateway.minecraft.commands.runners.ConfigCommands
import com.rose.gateway.minecraft.commands.runners.ConfigMonitoringRunner
import com.rose.gateway.minecraft.commands.runners.GeneralCommands
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CommandRegistry : KoinComponent {
    private val plugin: GatewayPlugin by inject()

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
                    runner(::ConfigValueArgs) { context ->
                        ConfigCommands.setConfiguration(context)
                    }
                }

                subcommand("add") {
                    runner(::ConfigValueArgs) { context ->
                        ConfigCommands.addConfiguration(context)
                    }
                }

                subcommand("remove") {
                    runner(::ConfigValueArgs) { context ->
                        ConfigCommands.removeConfiguration(context)
                    }
                }

                subcommand("help") {
                    runner(::ConfigItemArgs) { context ->
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
