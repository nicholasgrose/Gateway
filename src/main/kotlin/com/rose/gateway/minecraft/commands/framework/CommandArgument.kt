package com.rose.gateway.minecraft.commands.framework

interface CommandArgument<T> {
    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun noCompletionCompleter(tabCompletionContext: TabCompletionContext): MutableList<String>? {
            return null
        }

        fun subcommandCompleter(tabCompletionContext: TabCompletionContext): List<String>? {
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
            } else definition.subcommandNames.searchOrGetAll(rawArguments[0])
        }
    }

    fun fromString(string: String): T?
    fun getName(): String
    fun completeTab(tabCompletionContext: TabCompletionContext): List<String>?
}
