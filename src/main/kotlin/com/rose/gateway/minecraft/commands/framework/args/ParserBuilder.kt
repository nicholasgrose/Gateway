package com.rose.gateway.minecraft.commands.framework.args

import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext

/**
 * A builder for a single arg parser
 *
 * @param T The type of the value to be parsed
 * @param A The type of the args the parser is used for
 * @param P The type of the parser this builds
 * @constructor Create a parser builder with default settings
 */
abstract class ParserBuilder<T, A : CommandArgs<A>, P : ArgParser<T, A, P>> {
    /**
     * The name of the parser
     */
    lateinit var name: String

    /**
     * A description of the argument
     */
    lateinit var description: String

    /**
     * Whether the parser should continue to provide tab completions when successful
     */
    var completesAfterSatisfied = false

    /**
     * A function that is called to determine valid tab completions for the parser
     */
    var completer: P.(TabCompleteContext<A>) -> List<String> = { listOf() }

    /**
     * A function that is called to validate a successfully parsed value
     */
    var validator: P.(ParseResult.Success<T, A>) -> Boolean = { true }

    /**
     * A function that generates documentation for how the parser's arg is used
     */
    var usageGenerator: P.() -> List<String> = { listOf("[$name=${typeName()}]") }

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
