package com.rose.gateway.minecraft.commands.framework.data

import com.rose.gateway.minecraft.commands.framework.ArgumentParser

data class CommandExecutor(
    val executor: ((CommandContext) -> Boolean),
    val argumentParser: ArgumentParser
)
