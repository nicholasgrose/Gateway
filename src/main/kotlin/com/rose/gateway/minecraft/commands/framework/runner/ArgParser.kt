package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import kotlin.reflect.KProperty

abstract class ArgParser<
    T,
    A : CommandArgs<A>,
    P : ArgParser<T, A, P>
    >(
    private val builder: ParserBuilder<T, A, P>,
    private val completesAfterSatisfied: Boolean = false
) {
    fun name(): String = builder.name
    abstract fun typeName(): String
    fun completions(context: TabCompletionContext<A>): List<String> {
        return when {
            completesAfterSatisfied -> builder.completer(context)
            context.args.wasSuccessful(this) -> listOf()
            else -> builder.completer(context)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun generateUsages(args: A): List<String> = builder.usageGenerator(args, this as P)
    abstract fun parseValue(context: ParseContext<A>): ParseResult<T, A>

    fun parseValidValue(context: ParseContext<A>): ParseResult<T, A> {
        val parseResult = parseValue(context)

        return if (parseResult.succeeded) {
            val isValid = builder.validator(parseResult)

            ParseResult(
                succeeded = isValid,
                result = parseResult.result,
                context = parseResult.context
            )
        } else parseResult
    }

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: CommandArgs<A>, property: KProperty<*>): T? =
        thisRef.finalParseResult.result?.get(this)?.result as T?
}
