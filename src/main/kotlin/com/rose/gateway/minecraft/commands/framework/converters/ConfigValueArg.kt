package com.rose.gateway.minecraft.commands.framework.converters

import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.commands.framework.CommandArgument
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext

class ConfigValueArg(
    private val name: String,
    private val configNameArgIndex: Int,
    private val configuration: PluginConfiguration,
    private val tabCompleter: (TabCompletionContext) -> List<String>? = CommandArgument.Companion::noCompletionCompleter
) : CommandArgument<Any> {
    private val parserMap = mapOf(
        Boolean::class.javaObjectType to { value: String -> value.toBoolean() },
        Integer::class.javaObjectType to { value: String -> value.toInt() },
        String::class.javaObjectType to { value: String -> value },
    )

    override fun fromArguments(arguments: Array<String>, index: Int): Any? {
        if (!arguments.indices.contains(configNameArgIndex)) return null

        val configName = arguments[configNameArgIndex]
        val config = configuration.configurationStringMap.specificationFromString(configName) ?: return null
        val parser = parserMap[config.type.rawClass] ?: return null

        return try {
            parser(arguments[index])
        } catch (e: RuntimeException) {
            null
        }
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
