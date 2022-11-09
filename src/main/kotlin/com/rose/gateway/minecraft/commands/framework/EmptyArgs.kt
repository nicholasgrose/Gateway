package com.rose.gateway.minecraft.commands.framework

import com.rose.gateway.minecraft.commands.framework.runner.NoArgs

fun emptyArgs(args: List<String>): NoArgs = NoArgs().parseArguments(args)
