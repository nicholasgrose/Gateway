package com.rose.gateway.minecraft.commands.framework

class Checker(
    val converters: Array<out ArgumentConverter<*>>,
    private val variableArgumentNumberAllowed: Boolean
) {
    fun convertArguments(arguments: Array<String>): List<*>? {
        if (argumentCountIncorrect(arguments)) return null

        return converters.mapIndexed { index, converter ->
            converter.fromString(arguments[index]) ?: return@convertArguments null
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
