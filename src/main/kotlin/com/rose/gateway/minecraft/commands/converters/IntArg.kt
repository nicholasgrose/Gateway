package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.CommandArgument
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

@Deprecated("Might be useful in the future, but currently not used, in favor of ConfigValueArg.")
class IntArg(
    private val name: String,
    private val tabCompleter: (TabCompletionContext) -> List<String>? = CommandArgument.Companion::noCompletionCompleter
) : CommandArgument<Int> {
    override fun fromArguments(arguments: Array<String>, index: Int): Int? {
        return try {
            Integer.parseInt(arguments[index])
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
