package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.configuration.Item
import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.converters.configItem
import com.rose.gateway.minecraft.commands.converters.configValue
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

class ConfigListValueArgs : RunnerArguments<ConfigValueArgs>() {
    val configItem: Item<*>? by configItem {
        name = "CONFIG_PATH"
        description = "The config to modify."
        completer = ConfigCompleter::configNameCompletion
    }
    val configValue: List<Any?> by configListValue {
        name = "CONFIG_VALUE"
        description = "The value to give to the corresponding config."
        itemArg = ::configItem
    }
}

