package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
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
 * @param ListElementType Type for the elements in the list for the config
 * @param ListArgsType The type of the args this is
 * @param ListElementParserType The type of the parser that will get the individual list elements
 * @constructor Creates a config
 *
 * @param resultType The type of the list to associate with the args
 * @param valueArg The parser for the list arguments
 */
open class ConfigListArgs<
    ListElementType : Any,
    ListArgsType : ConfigListArgs<ListElementType, ListArgsType, ListElementParserType>,
    ListElementParserType : ArgParser<ListElementType, ListArgsType, ListElementParserType>
    >(
    resultType: KType,
    valueArg: ListArgsType.() -> ListParser<ListElementType, ListArgsType, ListElementParserType>
) : ConfigArgs<List<ListElementType>, ListArgsType, ListParser<ListElementType, ListArgsType, ListElementParserType>>(
    resultType,
    valueArg
)

/**
 * Config args for a list of strings
 *
 * @constructor Creates a string list config args
 *
 * @param stringCompleter The completer to use for each string
 * @param stringValidator The validator to use for each string
 */
class StringListConfigArgs(
    stringCompleter: (
        TabCompletionContext<StringListConfigArgs>
    ) -> List<String>,
    stringValidator: (ParseResult<String, StringListConfigArgs>) -> Boolean
) :
    ConfigListArgs<String, StringListConfigArgs, StringParser<StringListConfigArgs>>(
        typeOf<List<String>>(),
        {
            list {
                name = "VALUES"
                description = "Values to add."
                element = stringParser {
                    name = "VALUE"
                    description = "String to add."
                    completer = stringCompleter
                    validator = stringValidator
                }
            }
        }
    )

/**
 * Creates a string list config args for adding strings
 *
 * @return The constructed config args
 */
fun addStringListConfigArgs(): StringListConfigArgs = StringListConfigArgs(
    { listOf() },
    {
        val item = it.context.arguments.item

        item?.value?.contains(it.result)?.not() ?: false
    }
)

/**
 * Creates a string list config args for removing strings
 *
 * @return The constructed config args
 */
fun removeStringListConfigArgs(): StringListConfigArgs = StringListConfigArgs(
    {
        val currentValues = it.args.item?.value ?: listOf()
        val itemsSlatedForRemoval = it.args.value

        if (itemsSlatedForRemoval == null) {
            currentValues
        } else currentValues - itemsSlatedForRemoval.toSet()
    },
    {
        val item = it.context.arguments.item

        item?.value?.contains(it.result) ?: false
    }
)
