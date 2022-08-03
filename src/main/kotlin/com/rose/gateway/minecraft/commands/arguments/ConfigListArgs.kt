package com.rose.gateway.minecraft.commands.arguments

import com.rose.gateway.minecraft.commands.converters.ListArg
import com.rose.gateway.minecraft.commands.converters.StringArg
import com.rose.gateway.minecraft.commands.converters.list
import com.rose.gateway.minecraft.commands.converters.stringArg
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import kotlin.reflect.KType
import kotlin.reflect.typeOf

open class ConfigListArgs<
    T : Any,
    A : ConfigListArgs<T, A, R>,
    R : RunnerArg<T, A, R>
    >(
    resultType: KType,
    valueArg: A.() -> ListArg<T, A, R>
) : ConfigArgs<List<T>, A, ListArg<T, A, R>>(
    resultType,
    valueArg
)

class StringListConfigArgs(
    stringCompleter: (
        TabCompletionContext<StringListConfigArgs>
    ) -> List<String>,
    stringValidator: (ParseResult<String, StringListConfigArgs>) -> Boolean
) :
    ConfigListArgs<String, StringListConfigArgs, StringArg<StringListConfigArgs>>(
        typeOf<List<String>>(),
        {
            list {
                name = "VALUES"
                description = "Values to add."
                element = stringArg {
                    name = "VALUE"
                    description = "String to add."
                    completer = stringCompleter
                    validator = stringValidator
                }
                validator = {
                    when {
                        it.result == null -> false
                        it.result.isEmpty() -> false
                        else -> true
                    }
                }
            }
        }
    )

fun addStringListConfigArgs(): StringListConfigArgs = StringListConfigArgs(
    { listOf() },
    {
        val item = it.context.arguments.item

        item?.get()?.contains(it.result)?.not() ?: false
    }
)

fun removeStringListConfigArgs(): StringListConfigArgs = StringListConfigArgs(
    {
        val currentValues = it.arguments.item?.get() ?: listOf()
        val itemsSlatedForRemoval = it.arguments.value

        if (itemsSlatedForRemoval == null) {
            currentValues
        } else currentValues - itemsSlatedForRemoval.toSet()
    },
    {
        val item = it.context.arguments.item

        item?.get()?.contains(it.result) ?: false
    }
)
