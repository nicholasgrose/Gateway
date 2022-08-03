package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.configuration.ConfigurationStringMap
import com.rose.gateway.minecraft.commands.converters.configItem
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ConfigItemArgs : KoinComponent, RunnerArguments<ConfigItemArgs>() {
    private val stringMap: ConfigurationStringMap by inject()

    val item by configItem {
        name = "Config"
        description = "The config to use."
        completer = { stringMap.allStrings() }
    }
}
