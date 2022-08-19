package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.config.Item
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KType

fun <T : Any, A : RunnerArguments<A>> RunnerArguments<A>.typedConfigItem(
    body: GenericConfigItemArgBuilder<T, A>.() -> Unit
): GenericConfigItemArg<T, A> =
    genericParser(::GenericConfigItemArgBuilder, body)

class GenericConfigItemArg<T : Any, A : RunnerArguments<A>>(val builder: GenericConfigItemArgBuilder<T, A>) :
    RunnerArg<Item<T>, A, GenericConfigItemArg<T, A>>(builder), KoinComponent {
    val config: PluginConfig by inject()

    override fun typeName(): String = "ConfigItemType"

    private val internalStringParser = stringArg<A> {
        name = "CONFIG_INTERNAL"
        description = "Parses the string for the item."
    }

    override fun parseValue(context: ParseContext<A>): ParseResult<Item<T>, A> {
        val stringResult = internalStringParser.parseValue(context)

        return if (stringResult.succeeded && stringResult.result != null) {
            val nextString = stringResult.result
            val matchedConfig = config.get<T>(builder.type, nextString)

            ParseResult(
                succeeded = matchedConfig != null,
                result = matchedConfig,
                context = stringResult.context
            )
        } else failedParseResult(stringResult.context)
    }
}

class GenericConfigItemArgBuilder<T : Any, A : RunnerArguments<A>> :
    ArgBuilder<Item<T>, A, GenericConfigItemArg<T, A>>() {
    lateinit var type: KType

    override fun checkValidity() {
        if (!::type.isInitialized) error("No type give for item arg.")
    }

    override fun build(): GenericConfigItemArg<T, A> {
        return GenericConfigItemArg(this)
    }
}
