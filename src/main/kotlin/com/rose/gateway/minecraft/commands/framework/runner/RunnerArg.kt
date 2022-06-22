package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import kotlin.reflect.KProperty

abstract class RunnerArg<T, A : RunnerArguments<A>, R : RunnerArg<T, A, R>>(private val builder: ArgBuilder<T, A, R>) {
    open fun name(): String = builder.name
    abstract fun typeName(): String
    open fun completions(context: TabCompletionContext<A>): List<String>? = builder.completer(context)
    abstract fun parseValue(context: ParseContext<A>): ParseResult<T, A>

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: RunnerArguments<A>, property: KProperty<*>): T? =
        thisRef.finalParseResult.result?.get(this)?.result as T?
}
