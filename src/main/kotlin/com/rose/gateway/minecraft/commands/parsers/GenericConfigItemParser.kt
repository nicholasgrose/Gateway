package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.config.Item
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KType

fun <T : Any, A : CommandArgs<A>> CommandArgs<A>.typedConfigItem(
    body: GenericConfigItemParserBuilder<T, A>.() -> Unit
): GenericConfigItemParser<T, A> =
    genericParser(::GenericConfigItemParserBuilder, body)

class GenericConfigItemParser<T : Any, A : CommandArgs<A>>(val builder: GenericConfigItemParserBuilder<T, A>) :
    ArgParser<Item<T>, A, GenericConfigItemParser<T, A>>(builder), KoinComponent {
    val config: PluginConfig by inject()

    override fun typeName(): String = "ConfigItemType"

    private val internalStringParser = stringParser<A> {
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

class GenericConfigItemParserBuilder<T : Any, A : CommandArgs<A>> :
    ParserBuilder<Item<T>, A, GenericConfigItemParser<T, A>>() {
    lateinit var type: KType

    override fun checkValidity() {
        if (!::type.isInitialized) error("No type give for item arg.")
    }

    override fun build(): GenericConfigItemParser<T, A> {
        return GenericConfigItemParser(this)
    }
}
