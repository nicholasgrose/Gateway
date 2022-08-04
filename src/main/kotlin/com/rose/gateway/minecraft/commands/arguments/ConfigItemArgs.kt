package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.converters.configItem
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

class ConfigItemArgs : RunnerArguments<ConfigItemArgs>() {
    val item by configItem {
        name = "Config"
        description = "The config to use."
        completer = { ConfigCompleter.allConfigStrings() }
    }
}
