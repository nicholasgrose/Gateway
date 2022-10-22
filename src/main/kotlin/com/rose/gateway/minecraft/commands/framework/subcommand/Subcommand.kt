package com.rose.gateway.minecraft.commands.framework.subcommand

import com.rose.gateway.minecraft.commands.framework.CommandBuilder

/**
 * Adds a subcommand to this command
 *
 * @param name The name of the subcommand to add to this command
 * @param initializer The settings for this subcommand
 * @receiver This command builder
 */
fun CommandBuilder.subcommand(name: String, initializer: CommandBuilder.() -> Unit) {
    val newCommandBuilder = CommandBuilder(name)

    newCommandBuilder.apply(initializer)
    newCommandBuilder.parent = this

    children.add(newCommandBuilder.build())
}
