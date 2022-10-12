package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

abstract class ParserBuilder<
    T,
    A : CommandArgs<A>,
    P : ArgParser<T, A, P>
    > {
    lateinit var name: String
    lateinit var description: String
    var completesAfterSatisfied = false
    var completer: (TabCompletionContext<A>) -> List<String> = { listOf() }
    var validator: (ParseResult<T, A>) -> Boolean = { true }
    var usageGenerator: (A, P) -> List<String> = { _, arg -> listOf("[$name=${arg.typeName()}]") }

    abstract fun checkValidity()
    abstract fun build(): P

    fun buildAndCheck(): P {
        checkOwnValidity()
        checkValidity()

        return build()
    }

    private fun checkOwnValidity() {
        when {
            !::name.isInitialized -> error("no name given to argument")
            !::description.isInitialized -> error("no description given to argument")
        }
    }
}
