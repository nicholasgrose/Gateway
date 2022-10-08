package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.parsers.BooleanParser
import com.rose.gateway.minecraft.commands.parsers.StringParser
import com.rose.gateway.minecraft.commands.parsers.boolean
import com.rose.gateway.minecraft.commands.parsers.string
import com.rose.gateway.minecraft.commands.parsers.typedConfigItem
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Generic arguments for a config item and its value
 *
 * @param ConfigValueType The type of the config item
 * @param ConfigArgsType The type of arguments this class is
 * @param ConfigValueParserType The type of the parser for the config value, [ConfigValueType]
 * @constructor Create config args for the given config type and its parser
 *
 * @param configType The type of the config item
 * @param valueArg The parser for the config value
 *
 * @property item The config item referenced
 * @property value The config item's value
 */
abstract class ConfigArgs<
    ConfigValueType : Any,
    ConfigArgsType : ConfigArgs<ConfigValueType, ConfigArgsType, ConfigValueParserType>,
    ConfigValueParserType : ArgParser<ConfigValueType, ConfigArgsType, ConfigValueParserType>
    >(
    configType: KType,
    valueArg: ConfigArgsType.() -> ConfigValueParserType
) : CommandArgs<ConfigArgsType>() {
    val item by typedConfigItem<ConfigValueType, ConfigArgsType> {
        name = "CONFIG_ITEM"
        description = "The item to modify."
        type = configType
        completer = ConfigCompleter.configItemsWithType(configType)
    }

    @Suppress("UNCHECKED_CAST", "LeakingThis")
    val value by (this as ConfigArgsType).valueArg()
}

/**
 * Config args for boolean values
 *
 * @constructor Create config boolean args
 */
class ConfigBooleanArgs : ConfigArgs<Boolean, ConfigBooleanArgs, BooleanParser<ConfigBooleanArgs>>(
    typeOf<Boolean>(),
    {
        boolean {
            name = "VALUE"
            description = "Boolean to use the item with."
        }
    }
)

/**
 * Config args for string values
 *
 * @constructor Create config string args
 */
class ConfigStringArgs : ConfigArgs<String, ConfigStringArgs, StringParser<ConfigStringArgs>>(
    typeOf<String>(),
    {
        string {
            name = "VALUE"
            description = "String to use the item with."
        }
    }
)
