package com.rose.gateway.minecraft.commands.completers

import com.rose.gateway.config.ConfigStringMap
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.CommandArgs
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
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
    fun <T, A : CommandArgs<A>, P : ArgParser<T, A, P>>
    configStrings(): P.(TabCompleteContext<A>) -> List<String> =
        { configStringMap.allStrings() }

    /**
     * Gives a completer for the config items of a particular type
     *
     * @param T The type of the parser's value
     * @param A The type of the args the parser parses is for
     * @param P The type of the parser the completer is for
     * @param type The type of the config item to complete for
     * @return A completer for config items of a particular type
     */
    fun <T, A : CommandArgs<A>, P : ArgParser<T, A, P>> configItemsWithType(
        type: KType
    ): P.(TabCompleteContext<A>) -> List<String> {
        val items = config.allItems()
        val matchedItems = items.filter {
            val itemType = it.type

            itemType == type
        }

        return { matchedItems.map { it.path } }
    }
}
