package com.rose.gateway.minecraft.commands.framework.data.executor

import com.rose.gateway.minecraft.commands.framework.args.CommandArgs

/**
 * References a particular set of args that were saved in a command context
 *
 * @param A The type of the args being referenced
 * @property args The constructor for the args being referenced
 * @constructor Create an args ref
 *
 * @see CommandArgs
 */
data class ArgsReference<A : CommandArgs<A>>(
    val args: () -> A
)
