package com.rose.gateway.minecraft.commands.framework.data.context

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.data.executor.ArgsReference

/**
 * Info for a context that is specific to a parser in an args instance
 *
 * @param A The type of the args in this context
 * @property args The reference to the args this context is specific to
 * @property parser The parser this context is specific to
 * @constructor Create a parser-specific context
 */
data class ParserSpecificContext<A : CommandArgs<A>>(
    val args: ArgsReference<A>,
    val parser: ArgParser<*, A, *>
)
