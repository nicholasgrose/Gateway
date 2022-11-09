package com.rose.gateway.minecraft.commands.framework

/**
 * The builder for [MinecraftCommands]
 *
 * @constructor Create a minecraft commands builder
 *
 * @property commands The set of commands for the minecraft commands
 */
class MinecraftCommandsBuilder {
    val commands = mutableSetOf<MinecraftCommand>()

    /**
     * Add a new Minecraft command
     *
     * @param name The name of the command to add
     * @param initializer Configurations for this command
     * @receiver The command builder
     */
    fun command(name: String, initializer: CommandBuilder.() -> Unit) {
        val builder = CommandBuilder(name)

        builder.apply(initializer)

        commands.add(MinecraftCommand(builder.build()))
    }

    /**
     * Build the command group for this builder
     *
     * @return The created command group
     */
    fun build(): MinecraftCommands {
        return MinecraftCommands(this)
    }
}
