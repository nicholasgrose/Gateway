package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.CommandContext

class ExampleArguments : RunnerArguments<ExampleArguments>() {
    val arg1 by string {
        name = "Test1"
        description = "The value for test 1."
    }
    val arg2 by string {
        name = "Test2"
        description = "The value for test 2."
    }
}

class ExampleRunner : CommandRunner<ExampleArguments> {
    override fun runCommand(context: CommandContext<ExampleArguments>) {
        TODO("Not yet implemented")
    }
}
