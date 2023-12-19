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
/**
 * A builder for a single arg parser
 *
 * @param T The type of the value to be parsed
 * @param A The type of the args the parser is used for
 * @param P The type of the parser this builds
 * @property name The name of the parser
 * @property description The parser's description
 * @constructor Create a parser builder with default settings
 */
abstract class ParserBuilder<T, P, B>(
    var name: String,
    var description: String,
) where P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
    /**
     * Whether the parser should continue to provide tab completions when successful
     */
    var completesAfterSatisfied = false

    /**
     * A function that is called to determine valid tab completions for the parser
     */
    var completer: P.(TabCompleteContext) -> List<String> = { listOf() }

    /**
     * A function that is called to validate a successfully parsed value
     */
    var validator: P.(ParseResult.Success<T>) -> Boolean = { _ -> true }

    /**
     * A function that generates documentation for how the parser's arg is used
     */
    var usageGenerator: P.() -> List<String> = { listOf("[$name=${typeName()}]") }

    /**
     * Builds the parser from this parser builder
     *
     * @return The built parser
     */
    abstract fun build(): P
}
