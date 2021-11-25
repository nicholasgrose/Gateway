package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

class ArgumentParser(
    val converters: Array<out CommandArgument<*>>,
    private val variableArgumentNumberAllowed: Boolean
) {
    fun parseAllArguments(arguments: Array<String>): List<*>? {
        if (argumentCountIncorrect(arguments)) return null

        return parseArguments(arguments)
    }

    private fun argumentCountIncorrect(arguments: Array<String>): Boolean {
        return when {
            variableArgumentNumberAllowed -> arguments.size < converters.size
            !variableArgumentNumberAllowed -> arguments.size != converters.size
            else -> false
        }
    }

    fun parseArgumentSubset(arguments: Array<String>): List<*>? {
        if (argumentSubsetCountIncorrect(arguments)) return null

        return parseArguments(arguments)
    }

    private fun argumentSubsetCountIncorrect(arguments: Array<String>): Boolean {
        return when {
            !variableArgumentNumberAllowed -> arguments.size > converters.size
            else -> false
        }
    }

    @Suppress("RedundantNullableReturnType")
    private fun parseArguments(arguments: Array<String>): List<*>? {
        return arguments.mapIndexed { index, argument ->
            val converterIndex = minOf(index, converters.size - 1)
            converters[converterIndex].fromString(argument) ?: return@parseArguments null
        }
    }

    fun getTabCompletions(tabCompletionContext: TabCompletionContext): List<String>? {
        val parsedArguments = tabCompletionContext.parsedArguments
        val firstNullIndex = parsedArguments.indexOf(null)
        val lastParsedArgumentIndex = if (firstNullIndex == -1) parsedArguments.size - 1 else firstNullIndex
        val converterIndex = minOf(lastParsedArgumentIndex, converters.size - 1)

        return converters[converterIndex].completeTab(tabCompletionContext)
    }
}
