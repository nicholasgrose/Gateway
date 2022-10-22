package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.commands.arguments.ConfigBooleanArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigItemArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigStringArgs
import com.rose.gateway.minecraft.commands.arguments.addStringListConfigArgs
import com.rose.gateway.minecraft.commands.arguments.removeStringListConfigArgs
import com.rose.gateway.minecraft.commands.framework.minecraftCommands
import com.rose.gateway.minecraft.commands.framework.subcommand.subcommand
import com.rose.gateway.minecraft.commands.runners.BotCommands
import com.rose.gateway.minecraft.commands.runners.ConfigCommands
import com.rose.gateway.minecraft.commands.runners.ConfigMonitoringRunner
import com.rose.gateway.minecraft.commands.runners.GeneralCommands
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides functions for registering Minecraft commands
 */
object CommandRegistry : KoinComponent {
    private val plugin: GatewayPlugin by inject()

    /**
     * Registers all Minecraft commands
     */
    fun registerCommands() {
        commands.registerCommands(plugin)
    }

    private val commands = minecraftCommands {
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
                    runner(::ConfigBooleanArgs) { context ->
                        ConfigCommands.setConfig(context)
                    }

                    runner(::ConfigStringArgs) { context ->
                        ConfigCommands.setConfig(context)
                    }
                }

                subcommand("add") {
                    runner(::addStringListConfigArgs) { context ->
                        ConfigCommands.addConfiguration(context)
                    }
                }

                subcommand("remove") {
                    runner(::removeStringListConfigArgs) { context ->
                        ConfigCommands.removeConfiguration(context)
                    }
                }

                subcommand("help") {
                    runner(::ConfigItemArgs) { context ->
                        ConfigMonitoringRunner.sendConfigurationHelp(context)
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
