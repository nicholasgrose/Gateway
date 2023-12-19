package com.rose.gateway.minecraft.commands.framework.subcommand

import com.rose.gateway.minecraft.commands.framework.CommandBuilder
import com.rose.gateway.minecraft.commands.framework.args.StringArg
import com.rose.gateway.minecraft.commands.framework.args.stringArg

/**
 * Adds a subcommand to this command
 *
 * @param name The name of the subcommand to add to this command
 * @param initializer The settings for this subcommand
 * @receiver This command builder
 */
fun CommandBuilder.subcommand(name: String, initializer: CommandBuilder.() -> Unit) {
    stringArg({
        this@stringArg.name = name

        description = "The $name subcommand"
        usageGenerator = { listOf(name) }

        completer = { listOf(name) }
        validator = { result -> result.value == name }
    }) { _: StringArg ->
        initializer()
    }
}
