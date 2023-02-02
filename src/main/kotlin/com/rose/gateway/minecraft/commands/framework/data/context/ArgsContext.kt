package com.rose.gateway.minecraft.commands.framework.data.context

import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.data.executor.ArgsReference
import com.rose.gateway.shared.collections.bimap.Bimap

/**
 * Contains data about the arguments in the context
 *
 * @property raw The raw args given for this context
 * @property parsed The parsed arguments so far in this context
 * @constructor Create and args context
 */
data class ArgsContext(
    val raw: List<String>,
    val parsed: Bimap<ArgsReference<*>, CommandArgs<*>>
)
