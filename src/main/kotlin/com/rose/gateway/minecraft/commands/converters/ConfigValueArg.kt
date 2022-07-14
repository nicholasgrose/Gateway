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
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

fun <A : RunnerArguments<A>> RunnerArguments<A>.configValue(
    body: ConfigValueArgBuilder<A>.() -> Unit
): ConfigValueArg<A> =
    genericParser(::ConfigValueArgBuilder, body)

fun <A : RunnerArguments<A>> configValueArg(
    body: ConfigValueArgBuilder<A>.() -> Unit
): ConfigValueArg<A> =
    genericArgBuilder(::ConfigValueArgBuilder, body)

class ConfigValueArg<A : RunnerArguments<A>>(val builder: ConfigValueArgBuilder<A>) :
    RunnerArg<Any?, A, ConfigValueArg<A>>(builder), KoinComponent {
    val config: PluginConfiguration by inject()

    override fun typeName(): String = "ConfigValue"

    override fun parseValue(context: ParseContext<A>): ParseResult<Any?, A> {
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
        val parserMap = parserMap<T>()
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

    private fun <T> parserMap(): Map<KClass<out Any>, RunnerArg<out Any, A, *>> = mapOf(
        Boolean::class to booleanArg<A> {
            name = builder.name
            description = builder.description
        },
        Integer::class to intArg {
            name = builder.name
            description = builder.description
        },
        String::class to stringArg {
            name = builder.name
            description = builder.description
            hungry = true
        },
        List::class to listArg<T, A> {
            name = builder.name
            description = builder.description
        }
    )
}

class ConfigValueArgBuilder<A : RunnerArguments<A>> : ArgBuilder<Any?, A, ConfigValueArg<A>>() {
    lateinit var itemArg: KProperty<Item<*>?>

    override fun checkValidity() {
        if (!::itemArg.isInitialized) error("Corresponding ConfigItemArg not initialized for ConfigValueArg")
    }

    override fun build(): ConfigValueArg<A> {
        return ConfigValueArg(this)
    }
}
