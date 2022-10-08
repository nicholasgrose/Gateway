package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.parsers.configItem

/**
 * Arguments for an untyped, individual config item
 *
 * @constructor Create config item args
 */
class ConfigItemArgs : CommandArgs<ConfigItemArgs>() {
    val item by configItem {
        name = "Config"
        description = "The config to use."
        completer = ConfigCompleter.configStrings()
    }
}
