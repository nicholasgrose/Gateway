package com.rose.gateway.minecraft.commands.framework.data.definition

import com.rose.gateway.minecraft.commands.framework.data.executor.CommandExecutor

/**
 * Definition of a command
 *
 * @property name The command's name
 * @property executors The possible executors for this command in order of priority
 * @constructor Create a command definition
 */
data class CommandDefinition(
    val name: String,
    val executors: List<CommandExecutor<*, *, *>>,
)
