package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.converters.string
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ConfigNameArgs : KoinComponent, RunnerArguments<ConfigNameArgs>() {
    val config: PluginConfiguration by inject()

    val configPath: String? by string {
        name = "CONFIG_PATH"
        description = "The config to modify."
        completer = {
            ConfigCompleter.allConfigStrings()
        }
    }
}
