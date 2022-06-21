package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import kotlin.reflect.KProperty

interface RunnerArg<T, A : RunnerArguments<A>> {
    fun name(): String
    fun typeName(): String
    fun completions(context: TabCompletionContext<A>): List<String>?
    fun parseValue(context: ParseContext<A>): ParseResult<T, A>
    operator fun getValue(thisRef: RunnerArguments<A>, property: KProperty<*>): T
}

fun <A : RunnerArguments<A>> RunnerArguments<A>.string(body: StringArgBuilder<A>.() -> Unit): StringArg<A> {
    val builder = StringArgBuilder<A>()

    body(builder)

    val arg = builder.buildAndCheck()

    this.parsers.add(arg)

    return arg
}

class StringArg<A : RunnerArguments<A>>(private val builder: StringArgBuilder<A>) : RunnerArg<String, A> {
    override fun name(): String = builder.name
    override fun typeName(): String = String::class.simpleName.toString()
    override fun completions(context: TabCompletionContext<A>): List<String>? {
        TODO("Not yet implemented")
    }

    override fun parseValue(context: ParseContext<A>): ParseResult<String, A> {
        TODO("Not yet implemented")
    }

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
