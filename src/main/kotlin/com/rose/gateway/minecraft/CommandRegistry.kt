package com.rose.gateway.minecraft

import com.rose.gateway.minecraft.commands.BotCommands
import com.rose.gateway.minecraft.commands.ConfigCommands
import com.rose.gateway.minecraft.commands.GeneralCommands
import com.rose.gateway.minecraft.commands.framework.MinecraftCommandsBuilder.Companion.minecraftCommands
import com.rose.gateway.minecraft.commands.framework.converters.StringArg

object CommandRegistry {
    fun registerCommands() {
        commands.registerCommands()
    }

    private val commands = minecraftCommands {
        command("discord") {
            runner { context ->
                GeneralCommands.discordHelp(context)
            }
        }

        command("gateway") {
            subcommand("bot") {
                subcommand("restart") {
                    runner { BotCommands.restartBot() }
                }
            }

            subcommand("config") {
                subcommand("set") {
                    runner(StringArg("CONFIG_PATH"), StringArg("VALUE")) { context ->
                        ConfigCommands.setConfiguration(context)
                    }
                }

                subcommand("add") {
                    runner(StringArg("CONFIG_PATH"), StringArg("VALUE")) { context ->
                        ConfigCommands.addConfiguration(context)
                    }
                }

                subcommand("remove") {
                    runner(StringArg("CONFIGURATION_PATH"), StringArg("VALUE")) { context ->
                        ConfigCommands.removeConfiguration(context)
                    }
                }

                subcommand("help") {
                    runner(StringArg("CONFIGURATION_PATH")) { context ->
                        ConfigCommands.configurationHelp(context)
                    }
                    runner { context ->
                        ConfigCommands.configurationHelp(context)
                    }
                }
            }
        }
    }
}
