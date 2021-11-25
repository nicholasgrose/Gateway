package com.rose.gateway.minecraft.commands.framework.converters

import com.rose.gateway.minecraft.commands.framework.CommandArgument
import com.rose.gateway.minecraft.commands.framework.CommandArgument.Companion.noCompletionCompleter
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

class StringArg(
    private val name: String,
    private val tabCompleter: (TabCompletionContext) -> List<String>? = ::noCompletionCompleter
) : CommandArgument<String> {
    override fun fromString(string: String): String {
        return string
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
