package com.rose.gateway.minecraft

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.commands.arguments.ConfigBooleanArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigIntArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigItemArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigStringArgs
import com.rose.gateway.minecraft.commands.arguments.addStringListConfigArgs
import com.rose.gateway.minecraft.commands.arguments.removeStringListConfigArgs
import com.rose.gateway.minecraft.commands.framework.minecraftCommands
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
                runner(DiscordCommands::help)
            }
        }

        command("gateway") {
            subcommand("bot") {
                subcommand("restart") {
                    runner(BotCommands::restartBot)
                }

                subcommand("status") {
                    runner(BotCommands::botStatus)
                }
            }

            subcommand("config") {
                subcommand("set") {
                    runner(::ConfigBooleanArgs, ConfigCommands::setConfig)
                    runner(::ConfigIntArgs, ConfigCommands::setConfig)
                    runner(::ConfigStringArgs, ConfigCommands::setConfig)
                }
            }

            subcommand("add") {
                runner(::addStringListConfigArgs, ConfigCommands::addConfiguration)
            }

            subcommand("remove") {
                runner(::removeStringListConfigArgs, ConfigCommands::removeConfiguration)
            }

            subcommand("help") {
                runner(::ConfigItemArgs, ConfigMonitoringRunner::sendConfigurationHelp)

                runner(ConfigMonitoringRunner::sendAllConfigurationHelp)
            }

            subcommand("reload") {
                runner(ConfigMonitoringRunner::reloadConfig)
            }

            subcommand("save") {
                runner(ConfigMonitoringRunner::saveConfig)
            }

            subcommand("status") {
                runner(ConfigMonitoringRunner::sendConfigurationStatus)
            }
        }
    }
}
