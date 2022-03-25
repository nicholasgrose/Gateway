package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.minecraft.commands.framework.CommandArgument
import com.rose.gateway.minecraft.commands.framework.CommandArgument.Companion.noCompletionCompleter
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

class StringArg(
    private val name: String,
    private val tabCompleter: (TabCompletionContext) -> List<String>? = ::noCompletionCompleter
) : CommandArgument<String> {
    override fun fromArguments(arguments: Array<String>, index: Int): String {
        return arguments[index]
    }

    override fun getName(): String {
        return name
    }

    override fun getTypeName(): String {
        return "String"
    }

    override fun completeTab(tabCompletionContext: TabCompletionContext): List<String>? {
        return tabCompleter(tabCompletionContext)
    }
}
