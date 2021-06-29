package com.rose.gateway.minecraft.commands.framework

class MinecraftCommands(private val commands: List<Command>) {
    fun registerCommands() {
        commands.forEach { command -> command.registerCommand() }
    }
}
