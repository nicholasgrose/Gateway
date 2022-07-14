package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.configuration.Item
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KProperty

fun <A : RunnerArguments<A>> RunnerArguments<A>.configListValue(
    body: ConfigListValueArgBuilder<A>.() -> Unit
): ConfigListValueArg<A> =
    genericParser(::ConfigListValueArgBuilder, body)

class ConfigListValueArg<A : RunnerArguments<A>>(val builder: ConfigListValueArgBuilder<A>) :
    RunnerArg<List<Any?>, A, ConfigListValueArg<A>>(builder), KoinComponent {
    val config: PluginConfiguration by inject()

    override fun typeName(): String = "ConfigListValue"

    val internalParser = configValueArg<A> {
        name = builder.name
        description = builder.description
        itemArg = builder.itemArg
    }

    override fun parseValue(context: ParseContext<A>): ParseResult<List<Any?>, A> {
        val configItem = builder.itemArg.getter.call(context.arguments)
            ?: return ParseResult(
                succeeded = false,
                context = context,
                result = null
            )

        val result = parseValueForItem(context, configItem)

        return ParseResult(
            succeeded = result.succeeded,
            result = result.result,
            context = result.context
        )
    }



    private fun <T> parseValueForItem(context: ParseContext<A>, configItem: Item<T>): ParseResult<T, A> {
        val parser = parserMap[configItem.typeClass()]

        return if (parser == null) ParseResult(
            succeeded = false,
            result = null,
            context = context
        ) else {
            val parseResult = parser.parseValue(context)

            @Suppress("UNCHECKED_CAST")
            ParseResult(
                succeeded = parseResult.succeeded,
                result = parseResult.result as T?,
                context = parseResult.context
            )
        }
    }
}

class ConfigListValueArgBuilder<A : RunnerArguments<A>> : ArgBuilder<List<Any?>, A, ConfigListValueArg<A>>() {
    lateinit var itemArg: KProperty<Item<*>?>

    override fun checkValidity() {
        if (!::itemArg.isInitialized) error("Corresponding ConfigItemArg not initialized for ConfigListValueArg")
    }

    override fun build(): ConfigListValueArg<A> {
        return ConfigListValueArg(this)
    }
}
