package com.rose.gateway.minecraft

import com.rose.gateway.minecraft.commands.BotCommand
import com.rose.gateway.minecraft.commands.DiscordCommand
import com.rose.gateway.minecraft.commands.StringArg
import com.rose.gateway.minecraft.commands.minecraftCommand
import org.bukkit.plugin.java.JavaPlugin

object CommandRegistry {
    fun registerCommands(plugin: JavaPlugin) {
        plugin.getCommand(DiscordCommand.COMMAND_NAME)?.setExecutor(DiscordCommand())
        plugin.getCommand(BotCommand.COMMAND_NAME)?.setExecutor(BotCommand())
    }

    val commands = listOf(
        minecraftCommand("discord") {
            runner {
                it.sender.sendMessage("discord help")
            }
        },
        minecraftCommand("gateway") {
            command("bot") {
                command("restart") {
                    runner {
                        it.sender.sendMessage("bot restart")
                    }
                }
            }
            command("config") {
                command("set") {
                    runner(StringArg("configurationPath"), StringArg("value")) {
                        it.sender.sendMessage("config set")
                    }
                }
                command("add") {
                    runner(StringArg("configurationPath"), StringArg("value")) {
                        it.sender.sendMessage("config add")
                    }
                }
                command("remove") {
                    runner(StringArg("configurationPath"), StringArg("value")) {
                        it.sender.sendMessage("config remove")
                    }
                }
                command("help") {
                    runner() {

                    }
                }
            }
        }
    )
}
