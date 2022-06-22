package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

abstract class ArgBuilder<T, A : RunnerArguments<A>, R : RunnerArg<T, A, R>> {
    lateinit var name: String
    lateinit var description: String
    var completer: (TabCompletionContext<A>) -> List<String> = { listOf() }

    abstract fun checkValidity()
    abstract fun build(): R

    fun buildAndCheck(): R {
        checkOwnValidity()
        checkValidity()

        return build()
    }

    private fun checkOwnValidity() {
        if (!::name.isInitialized) error("no name given to argument")
        if (!::description.isInitialized) error("no description given to argument")
    }
}
