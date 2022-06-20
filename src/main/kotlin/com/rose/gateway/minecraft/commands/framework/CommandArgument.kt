package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

interface CommandArgument<T> {
    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun noCompletionCompleter(tabCompletionContext: TabCompletionContext): MutableList<String> {
            return mutableListOf()
        }
    }

    fun fromArguments(arguments: Array<String>, index: Int): T?
    fun getName(): String
    fun getTypeName(): String
    fun completeTab(tabCompletionContext: TabCompletionContext): List<String>?
}
