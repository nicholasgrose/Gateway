package com.rose.gateway.minecraft.commands.framework.runner

import com.rose.gateway.minecraft.commands.framework.data.CommandContext

interface CommandRunner<A : RunnerArguments<A>> {
    fun runCommand(context: CommandContext<A>)
}
