package com.rose.gateway.minecraft.commands.completers

import com.rose.gateway.configuration.ConfigurationStringMap
import com.rose.gateway.minecraft.commands.arguments.ConfigItemArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigNameArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigValueArgs
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConfigCompleter : KoinComponent {
    private val configStringMap: ConfigurationStringMap by inject()

    fun configNameArgsNameCompletion(context: TabCompletionContext<ConfigNameArgs>): List<String> {
        val currentConfigurationArgument = context.arguments.configPath ?: ""

        return configStringMap.matchingOrAllStrings(currentConfigurationArgument)
    }

    fun configItemArgsNameCompletion(context: TabCompletionContext<ConfigItemArgs>): List<String> {
        val currentConfigurationArgument = context.arguments.rawArguments.lastOrNull() ?: ""

        return configStringMap.matchingOrAllStrings(currentConfigurationArgument)
    }

    fun configValueArgsNameCompletion(context: TabCompletionContext<ConfigValueArgs>): List<String> {
        val currentConfigurationArgument = context.arguments.rawArguments.lastOrNull() ?: ""

        return configStringMap.matchingOrAllStrings(currentConfigurationArgument)
    }

//    fun collectionConfigNameCompletion(context: TabCompletionContext): List<String> {
//        return configNameCompletion(context).filter { config ->
//            configStringMap.fromString(config)?.typeClass()?.canBe(Collection::class) ?: false
//        }
//    }

    private val configValueCompletionMap = mapOf(
        Boolean::class to listOf("true", "false")
    )

    fun configValueCompletion(context: TabCompletionContext<ConfigValueArgs>): List<String> {
        val configItem = context.arguments.configItem ?: return listOf()

        return configValueCompletionMap[configItem.typeClass()] ?: listOf()
    }

//    fun collectionConfigValueCompletion(context: TabCompletionContext): List<String> {
//        val configName = context.parsedArguments.first() as String
//        val configItem = configStringMap.fromString(configName) ?: return listOf()
//
//        return if (configItem.typeClass() canBe List::class) {
//            @Suppress("UNCHECKED_CAST") val castItem = configItem as Item<List<*>>
//            castItem.get().map { it.toString() }
//        } else listOf()
//    }
}
