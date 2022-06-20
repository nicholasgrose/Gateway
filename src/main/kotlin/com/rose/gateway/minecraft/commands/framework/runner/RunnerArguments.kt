package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import kotlin.reflect.KProperty

open class RunnerArguments<A : RunnerArguments<A>>(
    val allowUnusedArguments: Boolean = false
) {
    val rawArguments: MutableList<String> = mutableListOf()
    private val parsers: List<ArgParser> = listOf()

    fun remainingArguments(): Array<String> {
        TODO()
    }

    fun valid(): Boolean {
        TODO()
    }

    open fun documentation(): String {
        return if (parsers.isEmpty()) ""
        else parsers.joinToString(
            separator = "] [",
            prefix = "[",
            postfix = "]"
        ) { parser -> "${parser.name}=${parser.type}" }
    }

    open fun completions(context: TabCompletionContext<A>): List<String>? {
        TODO()
    }
}

interface RunnerArg<T, A : RunnerArguments<A>> {
    operator fun getValue(thisRef: RunnerArguments<A>, property: KProperty<*>): T
}

fun <A : RunnerArguments<A>> RunnerArguments<A>.string(body: StringArgBuilder<A>.() -> Unit): StringArg<A> {
    val builder = StringArgBuilder<A>()

    body(builder)

    return builder.buildAndCheck()
}

class StringArg<A : RunnerArguments<A>>(val builder: StringArgBuilder<A>) : RunnerArg<String, A> {
    override fun getValue(thisRef: RunnerArguments<A>, property: KProperty<*>): String {
        TODO("Not yet implemented")
    }
}

class StringArgBuilder<A : RunnerArguments<A>> : ArgBuilder<StringArg<A>, A>() {
    override fun checkValidity() = Unit

    override fun build(): StringArg<A> {
        return StringArg(this)
    }
}
