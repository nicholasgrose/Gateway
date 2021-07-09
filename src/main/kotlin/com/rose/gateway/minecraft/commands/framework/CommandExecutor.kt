package com.rose.gateway.minecraft.commands.framework

data class CommandExecutor(
    val executor: ((CommandContext) -> Boolean),
    val argumentParser: ArgumentParser
)
