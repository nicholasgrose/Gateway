package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.commands.framework.args.booleanArg
import com.rose.gateway.minecraft.commands.framework.args.configArg
import com.rose.gateway.minecraft.commands.framework.args.intArg
import com.rose.gateway.minecraft.commands.framework.args.stringArg
import com.rose.gateway.minecraft.commands.framework.args.stringListArg
import com.rose.gateway.minecraft.commands.framework.args.typedConfigArg
import com.rose.gateway.minecraft.commands.framework.minecraftCommands
import com.rose.gateway.minecraft.commands.framework.subcommand.subcommand
import com.rose.gateway.minecraft.commands.parsers.stringParser
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
                    typedConfigArg<Boolean>({
                        name = "BooleanConfigItem"
                        description = "A config item that stores a Boolean"
                    }) { configArg ->
                        subcommand("set") {
                            booleanArg({
                                name = "Value"
                                description = "The value to give to a boolean config"
                            }) { booleanArg ->
                                executes { context ->
                                    ConfigCommands.setConfig(context, configArg, booleanArg)
                                }
                            }
                        }
                    }

                    typedConfigArg<Int>({
                        name = "IntConfigItem"
                        description = "A config item that stores an Int"
                    }) { configArg ->
                        subcommand("set") {
                            intArg({
                                name = "Value"
                                description = "The value to give to an int config"
                            }) { intArg ->
                                executes { context ->
                                    ConfigCommands.setConfig(context, configArg, intArg)
                                }
                            }
                        }
                    }

                    typedConfigArg<String>({
                        name = "StringConfigItem"
                        description = "A config item that stores a String"
                    }) { configArg ->
                        subcommand("set") {
                            stringArg({
                                name = "Value"
                                description = "The value to give to a string config"
                            }) { stringArg ->
                                executes { context ->
                                    ConfigCommands.setConfig(context, configArg, stringArg)
                                }
                            }
                        }
                    }

                    typedConfigArg<List<String>>({
                        name = "ListConfigItem"
                        description = "A config item that contains a list of values"
                    }) { configArg ->
                        subcommand("add") {
                            stringListArg({
                                name = "Values"
                                description = "String values to add to the config"
                                element = stringParser {
                                    name = "Value"
                                    description = "A value to add to the config"

                                    validator = { result ->
                                        val itemValue = result.context.valueOf(configArg).value
                                        val parsedValue = result.value

                                        itemValue.contains(parsedValue).not()
                                    }
                                }
                            }) { stringListArg ->
                                executes { context ->
                                    ConfigCommands.addConfiguration(context, configArg, stringListArg)
                                }
                            }
                        }

                        subcommand("remove") {
                            stringListArg({
                                name = "Values"
                                description = "String values to remove from the config"
                                element = stringParser {
                                    name = "Value"
                                    description = "A value to remove from the config"

                                    validator = { result ->
                                        val itemValue = result.context.valueOf(configArg).value
                                        val parsedValue = result.value

                                        itemValue.contains(parsedValue)
                                    }
                                }

                                completer = { context ->
                                    val itemValue = context.valueOf(configArg).value
                                    val parsedValues = context.valueOrNullOf(this) ?: listOf()

                                    itemValue - parsedValues
                                }
                            }) { stringListArg ->
                                executes { context ->
                                    ConfigCommands.removeConfiguration(context, configArg, stringListArg)
                                }
                            }
                        }
                    }
                }

                subcommand("help") {
                    configArg({
                        name = "Config"
                        description = "The config to use."
                    }) { configArg ->
                        executes { context ->
                            ConfigMonitoringRunner.sendConfigurationHelp(context, configArg)
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
