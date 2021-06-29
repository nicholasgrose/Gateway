package com.rose.gateway.minecraft

import com.rose.gateway.minecraft.commands.framework.MinecraftCommandsBuilder.Companion.minecraftCommands
import com.rose.gateway.minecraft.commands.framework.converters.StringArg

object CommandRegistry {
    fun registerCommands() {
        commands.registerCommands()
    }

    private val commands = minecraftCommands {
        baseCommand("discord") {
            runner {
                it.sender.sendMessage("discord help")
                true
            }
        }

        baseCommand("gateway") {
            command("bot") {
                command("restart") {
                    runner {
                        it.sender.sendMessage("bot restart")
                        true
                    }
                }
            }
            command("config") {
                command("set") {
                    runner(StringArg("CONFIG_PATH"), StringArg("VALUE")) {
                        it.sender.sendMessage("config set")
                        true
                    }
                }
                command("add") {
                    runner(StringArg("CONFIG_PATH"), StringArg("VALUE")) {
                        it.sender.sendMessage("config add")
                        true
                    }
                }
                command("remove") {
                    runner(StringArg("CONFIG_PATH"), StringArg("VALUE")) {
                        it.sender.sendMessage("config remove")
                        true
                    }
                }
                command("help") {
                    runner {
                        it.sender.sendMessage("config help")
                        true
                    }
                }
            }
        }
    }
}
