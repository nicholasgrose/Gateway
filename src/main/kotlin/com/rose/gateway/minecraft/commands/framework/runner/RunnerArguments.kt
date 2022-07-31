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

    fun remainingArguments(): Array<String> {
        return rawArguments.subList(finalParseResult.context.currentIndex, rawArguments.size).toTypedArray()
    }

    fun lastSuccessfulResult(): ParseResult<*, A>? {
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

    private fun hasUnusedArgs(): Boolean = rawArguments.size > finalParseResult.context.currentIndex

    fun argsParsed(): Int = lastSuccessfulResult()?.context?.currentIndex ?: 0

    open fun documentation(): String {
        return parsers.joinToString(" ") { parser ->
            @Suppress("UNCHECKED_CAST")
            parser.generateDocs(this as A)
        }
    }

    open fun completions(context: TabCompletionContext<A>): List<String> {
        if (argsParsed() + 1 < rawArguments.size) return listOf()

        val nextArg = parsers.firstOrNull {
            !(finalParseResult.result?.containsKey(it) ?: false)
        } ?: parsers.lastOrNull()

        return nextArg?.completions(context) ?: listOf()
    }
}
