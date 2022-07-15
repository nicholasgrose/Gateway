package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.configuration.Item
import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.converters.configItem
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

class ConfigItemArgs : RunnerArguments<ConfigItemArgs>() {
    val configItem: Item<*>? by configItem {
        name = "CONFIG_PATH"
        description = "The config to modify."
        completer = ConfigCompleter::configItemArgsNameCompletion
    }
}
