package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.GatewayPlugin

class MinecraftCommandsBuilder(val plugin: GatewayPlugin) {
    companion object {
        fun minecraftCommands(
            plugin: GatewayPlugin,
            initializer: MinecraftCommandsBuilder.() -> Unit
        ): MinecraftCommands {
            return build(plugin, MinecraftCommandsBuilder(plugin).apply(initializer))
        }

        private fun build(plugin: GatewayPlugin, builder: MinecraftCommandsBuilder): MinecraftCommands {
            return MinecraftCommands(plugin, builder.commands)
        }
    }

    val commands = mutableListOf<Command>()

    fun command(name: String, initializer: CommandBuilder.() -> Unit) {
        commands.add(CommandBuilder.build(CommandBuilder(name).apply(initializer)))
    }
}
