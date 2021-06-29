package com.rose.gateway.minecraft.commands.framework

class ArgumentParser(
    val converters: Array<out Parser<*>>,
    private val variableArgumentNumberAllowed: Boolean
) {
    fun parseArguments(arguments: Array<String>): List<*>? {
        if (argumentCountIncorrect(arguments)) return null

        return converters.mapIndexed { index, converter ->
            converter.fromString(arguments[index]) ?: return@parseArguments null
        }
    }

    private fun argumentCountIncorrect(arguments: Array<String>): Boolean {
        return if (variableArgumentNumberAllowed) {
            converters.size > arguments.size
        } else {
            converters.size != arguments.size
        }
    }
}
