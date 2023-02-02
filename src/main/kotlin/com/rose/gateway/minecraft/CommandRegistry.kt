package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.commands.arguments.ConfigBooleanArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigIntArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigItemArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigStringArgs
import com.rose.gateway.minecraft.commands.arguments.addStringListConfigArgs
import com.rose.gateway.minecraft.commands.arguments.removeStringListConfigArgs
import com.rose.gateway.minecraft.commands.framework.minecraftCommands
import com.rose.gateway.minecraft.commands.framework.nesting.args
import com.rose.gateway.minecraft.commands.framework.subcommand.subcommand
import com.rose.gateway.minecraft.commands.runners.BotCommands
import com.rose.gateway.minecraft.commands.runners.ConfigCommands
import com.rose.gateway.minecraft.commands.runners.ConfigMonitoringRunner
import com.rose.gateway.minecraft.commands.runners.DiscordCommands
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
        command("discord") {
            subcommand("help") {
                executes { context ->
                    DiscordCommands.help(context)
                }
            }
        }

        command("gateway") {
            subcommand("bot") {
                subcommand("restart") {
                    executes { context ->
                        BotCommands.restartBot(context)
                    }
                }

                subcommand("status") {
                    executes { context ->
                        BotCommands.botStatus(context)
                    }
                }
            }

            subcommand("config") {
                subcommand("change") {
                    args(::ConfigBooleanArgs) { configRef ->
                        subcommand("set") {
                            args(::ConfigBooleanArgs) { _ ->
                                executes { context ->
                                    ConfigCommands.setConfig(context, configRef)
                                }
                            }
                        }
                    }

                    args(::ConfigIntArgs) { configRef ->
                        subcommand("set") {
                            args(::ConfigIntArgs) { _ ->
                                executes { context ->
                                    ConfigCommands.setConfig(context, configRef)
                                }
                            }
                        }
                    }

                    args(::ConfigStringArgs) { configRef ->
                        subcommand("set") {
                            args(::ConfigStringArgs) { _ ->
                                executes { context ->
                                    ConfigCommands.setConfig(context, configRef)
                                }
                            }
                        }
                    }

                    args(::addStringListConfigArgs) { configRef ->
                        subcommand("add") {

                            args(::addStringListConfigArgs) { _ ->
                                executes { context ->
                                    ConfigCommands.addConfiguration(context, configRef)
                                }
                            }
                        }
                    }

                    args(::removeStringListConfigArgs) { configRef ->
                        subcommand("remove") {
                            args(::addStringListConfigArgs) { _ ->
                                executes { context ->
                                    ConfigCommands.addConfiguration(context, configRef)
                                }
                            }
                        }
                    }
                }

                subcommand("help") {
                    args(::ConfigItemArgs) { itemRef ->
                        executes { context ->
                            ConfigMonitoringRunner.sendConfigurationHelp(context, itemRef)
                        }
                    }

                    executes { context ->
                        ConfigMonitoringRunner.sendAllConfigurationHelp(context)
                    }
                }

                subcommand("reload") {
                    executes { context ->
                        ConfigMonitoringRunner.reloadConfig(context)
                    }
                }

                subcommand("save") {
                    executes { context ->
                        ConfigMonitoringRunner.saveConfig(context)
                    }
                }

                subcommand("status") {
                    executes { context ->
                        ConfigMonitoringRunner.sendConfigurationStatus(context)
                    }
                }
            }
        }
    }
}
