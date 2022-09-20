package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.converters.configItem
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments

/**
 * Arguments for an untyped, individual config item.
 *
 * @constructor Create config item args.
 */
class ConfigItemArgs : RunnerArguments<ConfigItemArgs>() {
    val item by configItem {
        name = "Config"
        description = "The config to use."
        completer = ConfigCompleter.configStrings()
    }
}
