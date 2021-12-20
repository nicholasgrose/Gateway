package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

interface CommandArgument<T> {
    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun noCompletionCompleter(tabCompletionContext: TabCompletionContext): MutableList<String> {
            return mutableListOf()
        }

        fun subcommandCompleter(tabCompletionContext: TabCompletionContext): List<String>? {
            val rawArguments = tabCompletionContext.rawArguments
            val definition = tabCompletionContext.commandDefinition

            return if (rawArguments.size > 1) {
                val subcommand = definition.subcommands[rawArguments.first()]
                subcommand?.onTabComplete(
                    tabCompletionContext.sender,
                    tabCompletionContext.command,
                    tabCompletionContext.alias,
                    rawArguments.subList(1, rawArguments.size).toTypedArray()
                )
            } else definition.subcommandNames.searchOrGetAll(rawArguments.first())
        }
    }

    fun fromArguments(arguments: Array<String>, index: Int): T?
    fun getName(): String
    fun getTypeName(): String
    fun completeTab(tabCompletionContext: TabCompletionContext): List<String>?
}
