package com.rose.gateway.minecraft.commands.framework

class ArgumentParser(
    val converters: Array<out CommandArgument<*>>,
    private val variableArgumentNumberAllowed: Boolean
) {
    fun parseArguments(arguments: Array<String>, argumentsToCheck: Int = arguments.size): List<*>? {
        if (argumentCountIncorrect(arguments, argumentsToCheck)) return null

        return arguments.mapIndexed { index, argument ->
            if (index >= argumentsToCheck) return@mapIndexed null
            val converterIndex = minOf(index, converters.size - 1)
            converters[converterIndex].fromString(argument) ?: return@parseArguments null
        }
    }

    private fun argumentCountIncorrect(arguments: Array<String>, argumentsToCheck: Int): Boolean {
        return when {
            arguments.size < argumentsToCheck -> true
            variableArgumentNumberAllowed -> converters.size > argumentsToCheck
            !variableArgumentNumberAllowed -> converters.size != argumentsToCheck
            else -> false
        }
    }

    fun getTabCompletions(tabCompletionContext: TabCompletionContext): MutableList<String>? {
        val parsedArguments = tabCompletionContext.parsedArguments
        val firstNullIndex = parsedArguments.indexOf(null)
        val lastParsedArgumentIndex = if (firstNullIndex == -1) parsedArguments.size - 1 else firstNullIndex
        val converterIndex = minOf(lastParsedArgumentIndex, converters.size - 1)

        return converters[converterIndex].completeTab(tabCompletionContext)
    }
}
