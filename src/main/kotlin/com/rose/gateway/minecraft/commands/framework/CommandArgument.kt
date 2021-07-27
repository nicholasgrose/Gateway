package com.rose.gateway.minecraft.commands.framework

interface CommandArgument<T> {
    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun noCompletionCompleter(tabCompletionContext: TabCompletionContext): MutableList<String>? {
            return null
        }

        fun subcommandCompleter(tabCompletionContext: TabCompletionContext): MutableList<String>? {
            val rawArguments = tabCompletionContext.rawArguments
            val definition = tabCompletionContext.commandDefinition

            return if (rawArguments.size > 1) {
                val subcommand = definition.subcommands[rawArguments[0]]
                subcommand?.onTabComplete(
                    tabCompletionContext.sender,
                    tabCompletionContext.command,
                    tabCompletionContext.alias,
                    rawArguments.subList(1, rawArguments.size).toTypedArray()
                )
            } else definition.subcommands.keys.toMutableList()
        }
    }

    fun fromString(string: String): T?
    fun getName(): String
    fun completeTab(tabCompletionContext: TabCompletionContext): MutableList<String>?
}
