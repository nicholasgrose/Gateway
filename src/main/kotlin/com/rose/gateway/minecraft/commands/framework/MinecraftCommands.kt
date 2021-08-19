package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.GatewayPlugin

class MinecraftCommands(private val plugin: GatewayPlugin, private val commands: List<Command>) {
    fun registerCommands() {
        commands.forEach { command -> command.registerCommand(plugin) }
    }
}
