package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

open class CommandArgs<A : CommandArgs<A>> {
    var rawArguments: List<String> = listOf()
    val parsers: MutableList<ArgParser<*, A, *>> = mutableListOf()
    var finalParseResult: ParseResult<MutableMap<ArgParser<*, A, *>, ParseResult<*, A>>, A> =
        fillFinalParseResults()

    fun forArguments(rawArgs: List<String>) {
        rawArguments = rawArgs

        finalParseResult = fillFinalParseResults()
    }

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

    private fun lastSuccessfulResult(): ParseResult<*, A>? {
        val finalResult = finalParseResult

        return if (finalResult is ParseResult.Success) {
            val lastSuccessfulArg = parsers.lastOrNull {
                finalResult.result.containsKey(it)
            } ?: return null

            finalResult.result[lastSuccessfulArg]
        } else null
    }

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

    private fun hasUnusedArgs(): Boolean = remainingArgs() > 0

    private fun remainingArgs(): Int {
        val lastIndex = lastSuccessfulResult()?.context?.currentIndex ?: 0

        return rawArguments.size - lastIndex
    }

    fun argsParsed(): Int = lastSuccessfulResult()?.context?.currentIndex ?: 0

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

    fun wasSuccessful(arg: ArgParser<*, A, *>): Boolean {
        val finalResult = finalParseResult

        return if (finalResult is ParseResult.Success) finalResult.result.containsKey(arg)
        else false
    }
}
