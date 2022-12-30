package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.args.NoArgs

/**
 * Creates a [NoArgs] instance that contains only a set of raw arguments
 *
 * @param args The raw arguments to be stored in the [NoArgs] instance
 * @return The constructed [NoArgs]
 *
 * @see NoArgs
 */
fun emptyArgs(args: List<String>): NoArgs = NoArgs().parseArguments(args)
