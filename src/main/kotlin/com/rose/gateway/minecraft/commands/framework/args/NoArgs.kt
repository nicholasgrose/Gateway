package com.rose.gateway.minecraft.commands.framework.args

import com.rose.gateway.minecraft.commands.framework.data.executor.ArgsReference

/**
 * A set of arguments that has no parsers defined
 *
 * @constructor Create a set of no args
 */
class NoArgs : CommandArgs<NoArgs>() {
    companion object {
        /**
         * A new reference to no command args
         */
        val ref
            get() = ArgsReference(::NoArgs)
    }
}
