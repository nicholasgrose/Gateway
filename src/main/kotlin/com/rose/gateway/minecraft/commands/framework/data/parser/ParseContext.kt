package com.rose.gateway.minecraft.commands.framework.data.parser

import com.rose.gateway.minecraft.commands.framework.args.CommandArgs

/**
 * The context surrounding parsing a value
 *
 * @param A The type of the args in this context
 * @property args The type of the args in this context
 * @property currentIndex The next index to use from the raw args
 * @constructor Create a parse context
 */
data class ParseContext<A : CommandArgs<A>>(
    val args: A,
    val currentIndex: Int
)
