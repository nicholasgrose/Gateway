package com.rose.gateway.minecraft.commands.completers

import com.rose.gateway.config.ConfigStringMap
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
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
    fun <T, A : CommandArgs<A>, P : ArgParser<T, A, P>> configStrings(): P.(TabCompletionContext<A>) -> List<String> =
        { configStringMap.allStrings() }

    fun <T, A : CommandArgs<A>, P : ArgParser<T, A, P>> configItemsWithType(
        type: KType
    ): P.(TabCompletionContext<A>) -> List<String> {
        val items = config.allItems()
        val matchedItems = items.filter {
            val itemType = it.type

            itemType == type
        }

        return { matchedItems.map { it.path } }
    }
}
