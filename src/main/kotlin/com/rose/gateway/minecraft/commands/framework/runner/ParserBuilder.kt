package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

/**
 * A builder for a single arg parser
 *
 * @param T The type of the value to be parsed
 * @param A The type of the args the parser is used for
 * @param P The type of the parser this builds
 * @constructor Create a parser builder with default settings
 *
 * @property name The name of the parser
 * @property description A description of the parser
 * @property completesAfterSatisfied Whether the parser should continue to provide tab completions when successful
 * @property completer A function that is called to determine valid tab completions for the parser
 * @property validator A function that is called to validate a successfully parsed value
 * @property usageGenerator A function that generates documentation for how the parser's arg is used
 */
abstract class ParserBuilder<T, A : CommandArgs<A>, P : ArgParser<T, A, P>> {
    lateinit var name: String
    lateinit var description: String
    var completesAfterSatisfied = false
    var completer: (TabCompletionContext<A>) -> List<String> = { listOf() }
    var validator: (ParseResult.Success<T, A>) -> Boolean = { true }
    var usageGenerator: (A, P) -> List<String> = { _, arg -> listOf("[$name=${arg.typeName()}]") }

    /**
     * Determines that this parser builder has valid settings
     */
    abstract fun checkValidity()

    /**
     * Builds the parser from this parser builder
     *
     * @return The built parser
     */
    abstract fun build(): P

    /**
     * Checks the validity of this parser builder and then builds its respective parser
     *
     * @return The built parser
     */
    fun buildAndCheck(): P {
        checkOwnValidity()
        checkValidity()

        return build()
    }

    /**
     * Checks the validity of thw base parser builder class
     */
    private fun checkOwnValidity() {
        when {
            !::name.isInitialized -> error("no name given to argument")
            !::description.isInitialized -> error("no description given to argument")
        }
    }
}
