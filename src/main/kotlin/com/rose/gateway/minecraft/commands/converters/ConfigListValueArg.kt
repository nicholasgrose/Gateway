package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.commands.framework.CommandArgument
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import com.rose.gateway.shared.configurations.canBe

class ConfigListValueArg(
    private val name: String,
    private val configNameArgIndex: Int,
    private val configuration: PluginConfiguration,
    private val tabCompleter: (TabCompletionContext) -> List<String>? = CommandArgument.Companion::noCompletionCompleter
) : CommandArgument<Any> {
    private val parserMap = mapOf(
        Boolean::class to { value: String -> value.toBooleanStrictOrNull() },
        Integer::class to { value: String -> value.toInt() },
        String::class to { value: String -> value },
    )

    override fun fromArguments(arguments: Array<String>, index: Int): Any? {
        if (!arguments.indices.contains(configNameArgIndex)) return null

        val parser = findArgumentParser(arguments)

        return try {
            parser?.invoke(arguments[index])
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun findArgumentParser(arguments: Array<String>): ((String) -> Any?)? {
        val configName = arguments[configNameArgIndex]
        val config = configuration.stringMap.fromString(configName) ?: return null
        val configType = config.typeClass()

        return if (configType canBe Collection::class) null
        else parserMap[configType]
    }

    override fun getName(): String {
        return name
    }

    override fun getTypeName(): String {
        return "ConfigType"
    }

    override fun completeTab(tabCompletionContext: TabCompletionContext): List<String>? {
        return tabCompleter(tabCompletionContext)
    }
}
