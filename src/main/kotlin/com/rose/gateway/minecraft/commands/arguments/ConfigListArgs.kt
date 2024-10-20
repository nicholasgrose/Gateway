package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.parsers.ListParser
import com.rose.gateway.minecraft.commands.parsers.StringParser
import com.rose.gateway.minecraft.commands.parsers.list
import com.rose.gateway.minecraft.commands.parsers.stringParser
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Arguments for a configuration that is a list of some values and some values for it
 *
 * @param T Type for the elements in the list for the config
 * @param A The type of the args this is
 * @param P The type of the parser that will get the individual list elements
 * @constructor Creates a config
 *
 * @param resultType The type of the list to associate with the args
 * @param valueArg The parser for the list arguments
 */
open class ConfigListArgs<T : Any, A : ConfigListArgs<T, A, P>, P : ArgParser<T, A, P>>(
    resultType: KType,
    valueArg: A.() -> ListParser<T, A, P>,
) : ConfigArgs<List<T>, A, ListParser<T, A, P>>(resultType, valueArg)

/**
 * Config args for a list of strings
 *
 * @constructor Creates a string list config args
 *
 * @param stringCompleter The completer to use for each string
 * @param stringValidator The validator to use for each string
 */
@Suppress("Indentation")
class StringListConfigArgs(
    private val stringCompleter: StringParser<StringListConfigArgs>.(
        TabCompleteContext<StringListConfigArgs>,
    ) -> List<String>,
    private val stringValidator: StringParser<StringListConfigArgs>.(
        ParseResult.Success<String, StringListConfigArgs>,
    ) -> Boolean,
) : ConfigListArgs<String, StringListConfigArgs, StringParser<StringListConfigArgs>>(
        typeOf<List<String>>(),
        StringListConfigArgs::parser,
    ) {
    /**
     * Creates the list parser for these string list config args
     *
     * @return THe constructed parser
     */
    fun parser(): ListParser<String, StringListConfigArgs, StringParser<StringListConfigArgs>> =
        list {
            name = "VALUES"
            description = "Values to add."
            element =
                stringParser {
                    name = "VALUE"
                    description = "String to add."
                    completer = stringCompleter
                    validator = stringValidator
                }
        }
}

/**
 * Creates a string list config args for adding strings
 *
 * @return The constructed config args
 */
fun addStringListConfigArgs(): StringListConfigArgs =
    StringListConfigArgs(
        { listOf() },
        {
            val item = it.context.args.item

            item.value.contains(it.result).not()
        },
    )

/**
 * Creates a string list config args for removing strings
 *
 * @return The constructed config args
 */
fun removeStringListConfigArgs(): StringListConfigArgs =
    StringListConfigArgs(
        { context ->
            val currentValues = context.args.item.value
            val itemsSlatedForRemoval = existingValues(context)

            currentValues - itemsSlatedForRemoval.toSet()
        },
        { parseResult ->
            val item = parseResult.context.args.item

            item.value.contains(parseResult.result)
        },
    )

private fun existingValues(context: TabCompleteContext<StringListConfigArgs>): List<String> =
    if (context.args.wasSuccessful(context.completingParser)) {
        context.args.value
    } else {
        listOf()
    }
