package com.rose.gateway.minecraft.commands.framework

class MinecraftCommandsBuilder {
    companion object {
        fun minecraftCommands(initializer: MinecraftCommandsBuilder.() -> Unit): MinecraftCommands {
            return build(MinecraftCommandsBuilder().apply(initializer))
        }

        private fun build(builder: MinecraftCommandsBuilder): MinecraftCommands {
            return MinecraftCommands(builder.commands)
        }
    }

    val commands = mutableListOf<Command>()

    fun baseCommand(name: String, initializer: CommandBuilder.() -> Unit) {
        commands.add(CommandBuilder.build(CommandBuilder(name).apply(initializer)))
    }
}
