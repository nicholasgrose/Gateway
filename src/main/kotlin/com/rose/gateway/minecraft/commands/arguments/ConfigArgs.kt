package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.converters.BooleanArg
import com.rose.gateway.minecraft.commands.converters.StringArg
import com.rose.gateway.minecraft.commands.converters.boolean
import com.rose.gateway.minecraft.commands.converters.string
import com.rose.gateway.minecraft.commands.converters.typedConfigItem
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Generic arguments for a config item and its value.
 *
 * @param T The type of the config item.
 * @param A The type of arguments this class is.
 * @param R The type of the parser for the config value, [T].
 * @constructor Create config args for the given config type and its parser.
 *
 * @param configType The type of the config item.
 * @param valueArg The parser for the config value.
 *
 * @property item The config item referenced.
 * @property value The config item's value.
 */
abstract class ConfigArgs<
    T : Any,
    A : ConfigArgs<T, A, R>,
    R : RunnerArg<T, A, R>
    >(
    configType: KType,
    valueArg: A.() -> R
) : RunnerArguments<A>() {
    val item by typedConfigItem<T, A> {
        name = "CONFIG_ITEM"
        description = "The item to modify."
        type = configType
        completer = ConfigCompleter.configItemsWithType(configType)
    }

    @Suppress("UNCHECKED_CAST", "LeakingThis")
    val value by (this as A).valueArg()
}

/**
 * Config boolean args
 *
 * @constructor Create empty Config boolean args
 */
class ConfigBooleanArgs : ConfigArgs<Boolean, ConfigBooleanArgs, BooleanArg<ConfigBooleanArgs>>(
    typeOf<Boolean>(),
    {
        boolean {
            name = "VALUE"
            description = "Boolean to use the item with."
        }
    }
)

/**
 * Config string args
 *
 * @constructor Create empty Config string args
 */
class ConfigStringArgs : ConfigArgs<String, ConfigStringArgs, StringArg<ConfigStringArgs>>(
    typeOf<String>(),
    {
        string {
            name = "VALUE"
            description = "String to use the item with."
        }
    }
)
