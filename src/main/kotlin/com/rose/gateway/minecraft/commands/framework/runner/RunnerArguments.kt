package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

open class RunnerArguments<A : RunnerArguments<A>> {
    var rawArguments: List<String> = listOf()
    val parsers: MutableList<RunnerArg<*, A, *>> = mutableListOf()
    var finalParseResult: ParseResult<MutableMap<RunnerArg<*, A, *>, ParseResult<*, A>>, A> = fillFinalParseResults()

    fun forArguments(rawArgs: List<String>) {
        rawArguments = rawArgs

        finalParseResult = fillFinalParseResults()
    }

    private fun fillFinalParseResults(): ParseResult<MutableMap<RunnerArg<*, A, *>, ParseResult<*, A>>, A> {
        @Suppress("UNCHECKED_CAST")
        var currentContext = ParseContext(this as A, 0)
        val resultMap = mutableMapOf<RunnerArg<*, A, *>, ParseResult<*, A>>()
        finalParseResult = ParseResult(succeeded = true, result = resultMap, context = currentContext)

        for (parser in parsers) {
            val result = parser.parseValidValue(currentContext)

            currentContext = result.context

            if (result.succeeded) {
                resultMap[parser] = result

                finalParseResult = ParseResult(succeeded = true, result = resultMap, context = currentContext)
            } else {
                return ParseResult(
                    succeeded = false,
                    result = resultMap,
                    context = currentContext
                )
            }
        }

        return ParseResult(
            succeeded = true,
            result = resultMap,
            context = currentContext
        )
    }

    private fun lastSuccessfulResult(): ParseResult<*, A>? {
        val lastSuccessfulArg = parsers.lastOrNull {
            finalParseResult.result?.containsKey(it) ?: false
        } ?: return null

        return finalParseResult.result?.get(lastSuccessfulArg)
    }

    fun valid(): Boolean {
        if (hasUnusedArgs()) {
            return false
        }

        return parsers.all { finalParseResult.result?.get(it)?.succeeded ?: false }
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

    fun wasSuccessful(arg: RunnerArg<*, A, *>): Boolean = finalParseResult.result?.containsKey(arg) ?: false
}
