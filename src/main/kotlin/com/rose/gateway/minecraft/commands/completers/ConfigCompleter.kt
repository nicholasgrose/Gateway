package com.rose.gateway.minecraft.commands.completers

import com.rose.gateway.config.ConfigStringMap
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KType

/**
 * Contains completer functions for config items
 *
 * @constructor Create empty Config completer
 */
object ConfigCompleter : KoinComponent {
    private val config: PluginConfig by inject()
    private val configStringMap: ConfigStringMap by inject()

    /**
     * Gives a completion function for config strings
     *
     * @param A The argument type for the function's completion context
     * @return The completer for config strings
     */
    fun <A : RunnerArguments<A>> configStrings(): (TabCompletionContext<A>) -> List<String> =
        { configStringMap.allStrings() }

    /**
     * Completion for config items with a particular type
     *
     * @param A The argument type for the function's completion context
     * @param type The type for the config item
     * @return The completer for config items of the given type
     */
    fun <A : RunnerArguments<A>> configItemsWithType(type: KType): (TabCompletionContext<A>) -> List<String> {
        val items = config.allItems()
        val matchedItems = items.filter {
            val itemType = it.type

            itemType == type
        }

        return { matchedItems.map { it.path } }
    }
}
