package com.rose.gateway.minecraft.commands.completers

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import com.uchuhimo.konf.Item

class ConfigCompleter(val plugin: GatewayPlugin) {
    private val config = plugin.configuration
    private val configStringMap = config.configurationStringMap

    fun configNameCompletion(context: TabCompletionContext): List<String> {
        val currentConfigurationArgument = context.parsedArguments.last() as String

        return configStringMap.matchingOrAllConfigurationStrings(currentConfigurationArgument)
    }

    fun collectionConfigNameCompletion(context: TabCompletionContext): List<String> {
        return configNameCompletion(context).filter { config ->
            configStringMap.specificationFromString(config)?.type?.isCollectionLikeType ?: false
        }
    }

    private val configValueCompletionMap = mapOf(
        Boolean::class.javaObjectType to listOf("true", "false")
    )

    fun configValueCompletion(context: TabCompletionContext): List<String> {
        val configName = context.parsedArguments.first() as String

        val configItem = configStringMap.specificationFromString(configName) ?: return listOf()

        return configValueCompletionMap[configItem.type.rawClass] ?: listOf()
    }

    fun collectionConfigValueCompletion(context: TabCompletionContext): List<String> {
        val configName = context.parsedArguments.first() as String

        @Suppress("UNCHECKED_CAST")
        val configItem = configStringMap.specificationFromString(configName) as Item<List<*>>?

        return if (configItem == null) listOf()
        else config[configItem]?.map { it.toString() } ?: listOf()
    }
}
