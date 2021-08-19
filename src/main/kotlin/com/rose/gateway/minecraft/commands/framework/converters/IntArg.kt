package com.rose.gateway.minecraft.commands.framework.converters

import com.rose.gateway.minecraft.commands.framework.CommandArgument
import com.rose.gateway.minecraft.commands.framework.TabCompletionContext

class IntArg(
    private val name: String,
    private val tabCompleter: (TabCompletionContext) -> List<String>? = CommandArgument.Companion::noCompletionCompleter
) : CommandArgument<Int> {
    override fun fromString(string: String): Int? {
        return try {
            Integer.parseInt(string)
        } catch (e: NumberFormatException) {
            null
        }
    }

    override fun getName(): String {
        return name
    }

    override fun completeTab(tabCompletionContext: TabCompletionContext): List<String>? {
        return tabCompleter(tabCompletionContext)
    }

    override fun getTypeName(): String {
        return "Integer"
    }
}
