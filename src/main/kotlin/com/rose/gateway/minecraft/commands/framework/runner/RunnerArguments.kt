package com.rose.gateway.minecraft.commands.framework.runner

import kotlin.reflect.KProperty

open class RunnerArguments<A : RunnerArguments<A>> {
    val parsers: List<ArgParser> = listOf()
    var rawArguments: List<String> = listOf()
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
    override operator fun getValue(thisRef: RunnerArguments<A>, property: KProperty<*>): String {
        TODO("Not yet implemented")
    }
}

class StringArgBuilder<A : RunnerArguments<A>> : ArgBuilder<StringArg<A>, A>() {
    override fun checkValidity() = Unit

    override fun build(): StringArg<A> {
        return StringArg(this)
    }
}
