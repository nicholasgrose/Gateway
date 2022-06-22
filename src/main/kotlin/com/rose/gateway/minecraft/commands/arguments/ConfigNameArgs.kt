package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.converters.string
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

open class ConfigNameArgs : RunnerArguments<ConfigNameArgs>() {
    val configPath: String? by string {
        name = "CONFIG_PATH"
        description = "The config to modify."
        completer = ConfigCompleter::configNameCompletion
    }
}
