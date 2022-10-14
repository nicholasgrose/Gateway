package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

/**
 * A set of arguments for a command
 *
 * @param A The type of these arguments
 * @constructor Create empty command arguments
 *
 * @property rawArguments The raw, unparsed command arguments that came in
 * @property parsers The parsers that parse the raw arguments for these arguments
 * @property finalParseResult The result of parsing the raw arguments with all stored parsers
 */
open class CommandArgs<A : CommandArgs<A>> {
    var rawArguments: List<String> = listOf()
    val parsers: MutableList<ArgParser<*, A, *>> = mutableListOf()
    var finalParseResult: ParseResult<MutableMap<ArgParser<*, A, *>, ParseResult<*, A>>, A> =
        fillFinalParseResults()

    /**
     * Fills in this argument's [finalParseResult] for a set of raw arguments
     *
     * @param rawArgs The raw arguments to parse for the final result
     */
    fun forArguments(rawArgs: List<String>) {
        rawArguments = rawArgs
        finalParseResult = fillFinalParseResults()
    }

    /**
     * This constructs the [finalParseResult] by running all parsers on the [rawArguments].
     * It fills [finalParseResult] with intermediate values each time a parser succeeds.
     *
     * @return The final result of running all parsers
     */
    private fun fillFinalParseResults(): ParseResult<MutableMap<ArgParser<*, A, *>, ParseResult<*, A>>, A> {
        @Suppress("UNCHECKED_CAST")
        var currentContext = ParseContext(this as A, 0)
        val resultMap = mutableMapOf<ArgParser<*, A, *>, ParseResult<*, A>>()
        finalParseResult = ParseResult.Success(resultMap, currentContext)

        for (parser in parsers) {
            val result = parser.parseValidValue(currentContext)

            currentContext = result.context

            if (result is ParseResult.Success) {
                resultMap[parser] = result

                finalParseResult = ParseResult.Success(resultMap, currentContext)
            } else {
                return ParseResult.Failure(currentContext)
            }
        }

        return ParseResult.Success(resultMap, currentContext)
    }

    /**
     * Determines which parse result was the last one to finish successfully
     *
     * @return The most recently successful parse result or null if none could be found
     */
    private fun lastSuccessfulResult(): ParseResult<*, A>? {
        val finalResult = finalParseResult

        return if (finalResult is ParseResult.Success) {
            val lastSuccessfulArg = parsers.lastOrNull {
                finalResult.result.containsKey(it)
            } ?: return null

            finalResult.result[lastSuccessfulArg]
        } else null
    }

    /**
     * This determines whether these arguments are valid.
     * Validity is defined as all parsers having parsed successfully without leaving any arguments unused.
     *
     * @return Whether these arguments are valid
     */
    fun valid(): Boolean {
        if (hasUnusedArgs()) {
            return false
        }

        return parsers.all {
            val finalResult = finalParseResult

            if (finalResult is ParseResult.Success) finalResult.result[it] is ParseResult.Success
            else false
        }
    }

    /**
     * Determines whether any raw arguments are unused by parsers
     *
     * @return Whether there are any unused args
     */
    private fun hasUnusedArgs(): Boolean = remainingArgs() > 0

    /**
     * How many raw args are not used by any parsers
     *
     * @return The number of unused args remaining
     */
    private fun remainingArgs(): Int {
        val lastIndex = lastSuccessfulResult()?.context?.currentIndex ?: 0

        return rawArguments.size - lastIndex
    }

    /**
     * Determines the number of raw arguments that have been parsed
     *
     * @return the number of raw arguments that have been parsed
     */
    fun argsParsed(): Int = lastSuccessfulResult()?.context?.currentIndex ?: 0

    /**
     * Determines all possible valid usages for these args
     *
     * @return All possible usages for these args
     */
    fun usages(): List<String> {
        val parserUsages = parsers.map {
            @Suppress("UNCHECKED_CAST")
            it.generateUsages(this as A)
        }
        var allUsages = listOf<String>()

        for (usageList in parserUsages) {
            if (usageList.isEmpty()) continue

            allUsages = if (allUsages.isEmpty()) {
                usageList.toList()
            } else {
                usageList.flatMap { usage ->
                    allUsages.map { existingUsage -> "$existingUsage $usage" }
                }.toList()
            }
        }

        return allUsages
    }

    /**
     * Determines the valid tab completions for these args in the given context
     *
     * @param context The context to complete these args within
     * @return All completions this arg has in the given context
     */
    fun completions(context: TabCompletionContext<A>): List<String> {
        val nextArg = if (hasUnusedArgs()) {
            parsers.firstOrNull {
                !(wasSuccessful(it))
            } ?: parsers.lastOrNull()
        } else {
            parsers.lastOrNull {
                wasSuccessful(it)
            } ?: parsers.firstOrNull()
        }

        return when {
            remainingArgs() > 1 -> listOf()
            else -> nextArg?.completions(context) ?: listOf()
        }
    }

    /**
     * Determines whether a particular arg was successfully parsed
     *
     * @param arg The arg to check the parse status of
     * @return Whether a parsed value exists for the arg
     */
    fun wasSuccessful(arg: ArgParser<*, A, *>): Boolean {
        val finalResult = finalParseResult

        return if (finalResult is ParseResult.Success) finalResult.result.containsKey(arg)
        else false
    }
}
