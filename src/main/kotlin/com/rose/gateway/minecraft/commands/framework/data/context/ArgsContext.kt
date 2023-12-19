package com.rose.gateway.minecraft.commands.framework.data.context

import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.ParseResult

/**
 * Contains data about the arguments in the context
 *
 * @property raw The raw args given for this context
 * @property ArgParsers A map of all arg parsers to their respective arg refs
 * @property parsed A map of all arg refs to the parse results that they reference
 * @constructor Create and args context
 */
data class ArgsContext(
    val raw: List<String>,
    val parsed: MutableMap<ArgParser<*, *, *>, ParseResult<*>>,
)
