package com.rose.gateway.minecraft.commands.framework.subcommand

import com.rose.gateway.minecraft.commands.framework.CommandBuilder

fun CommandBuilder.subcommand(name: String, initializer: CommandBuilder.() -> Unit) {
    val newCommandBuilder = CommandBuilder(name)

    newCommandBuilder.apply(initializer)
    newCommandBuilder.parent = this

    children.add(newCommandBuilder.build())
}
