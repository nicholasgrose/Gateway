package com.rose.gateway.minecraft

import com.rose.gateway.minecraft.commands.BotCommand
import com.rose.gateway.minecraft.commands.DiscordCommand
import com.rose.gateway.minecraft.commands.StringArg
import com.rose.gateway.minecraft.commands.minecraftCommand
import org.bukkit.plugin.java.JavaPlugin

object CommandRegistry {
    fun registerCommands() {
        commands.forEach { command -> command.registerCommand() }
    }

    val commands = listOf(
        minecraftCommand("discord") {
            runner {
                it.sender.sendMessage("discord help")
                true
            }
        },
        minecraftCommand("gateway") {
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
                    runner(StringArg("configurationPath"), StringArg("value")) {
                        it.sender.sendMessage("config set")
                        true
                    }
                }
                command("add") {
                    runner(StringArg("configurationPath"), StringArg("value")) {
                        it.sender.sendMessage("config add")
                        true
                    }
                }
                command("remove") {
                    runner(StringArg("configurationPath"), StringArg("value")) {
                        it.sender.sendMessage("config remove")
                        true
                    }
                }
                command("help") {
                    runner() {
                        it.sender.sendMessage("config help")
                        true
                    }
                }
            }
        }
    )
}
