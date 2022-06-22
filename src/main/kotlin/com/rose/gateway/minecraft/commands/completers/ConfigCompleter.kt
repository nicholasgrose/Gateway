package com.rose.gateway.minecraft.commands.completers

import com.rose.gateway.configuration.ConfigurationStringMap
import com.rose.gateway.configuration.Item
import com.rose.gateway.minecraft.commands.arguments.ConfigNameArgs
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import com.rose.gateway.shared.configurations.canBe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConfigCompleter : KoinComponent {
    private val configStringMap: ConfigurationStringMap by inject()

    fun configNameCompletion(context: TabCompletionContext<ConfigNameArgs>): List<String> {
        val currentConfigurationArgument = context.arguments.configPath ?: ""

        return configStringMap.matchingOrAllStrings(currentConfigurationArgument)
    }

    fun collectionConfigNameCompletion(context: TabCompletionContext): List<String> {
        return configNameCompletion(context).filter { config ->
            configStringMap.fromString(config)?.typeClass()?.canBe(Collection::class) ?: false
        }
    }

    private val configValueCompletionMap = mapOf(
        Boolean::class to listOf("true", "false")
    )

    fun configValueCompletion(context: TabCompletionContext): List<String> {
        val configName = context.parsedArguments.first() as String

        val configItem = configStringMap.fromString(configName) ?: return listOf()

        return configValueCompletionMap[configItem.typeClass()] ?: listOf()
    }

    fun collectionConfigValueCompletion(context: TabCompletionContext): List<String> {
        val configName = context.parsedArguments.first() as String
        val configItem = configStringMap.fromString(configName) ?: return listOf()

        return if (configItem.typeClass() canBe List::class) {
            @Suppress("UNCHECKED_CAST") val castItem = configItem as Item<List<*>>
            castItem.get().map { it.toString() }
        } else listOf()
    }
}
