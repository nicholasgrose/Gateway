package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.config.Item
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.commands.framework.data.parser.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.ParserBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KType

/**
 * Adds a typed config item argument to these args
 *
 * @param A The type of the [CommandArgs] this parser is a part of
 * @param body The arg body
 * @receiver The builder for the parser
 * @return The built parser
 */
fun <T : Any, A : CommandArgs<A>> CommandArgs<A>.typedConfigItem(
    body: TypedConfigItemParserBuilder<T, A>.() -> Unit
): TypedConfigItemParser<T, A> = genericParser(::TypedConfigItemParserBuilder, body)

/**
 * Parser for a typed config item argument
 *
 * @param T The type of the config item's value
 * @param A The type of the args this parser is used by
 * @constructor Creates a typed config item parser
 *
 * @param builder The builder that defines this parser
 */
class TypedConfigItemParser<T : Any, A : CommandArgs<A>>(override val builder: TypedConfigItemParserBuilder<T, A>) :
    ArgParser<Item<T>, A, TypedConfigItemParser<T, A>>(builder), KoinComponent {
    private val config: PluginConfig by inject()

    override fun typeName(): String = "ConfigItemType"

    private val internalStringParser = stringParser<A> {
        name = "CONFIG_INTERNAL"
        description = "Parses the string for the item."
    }

    override fun parseValue(context: ParseContext<A>): ParseResult<Item<T>, A> {
        val stringResult = internalStringParser.parseValue(context)

        return if (stringResult is ParseResult.Success) {
            val nextString = stringResult.result
            val matchedConfig = config.get<T>(builder.type, nextString)

            if (matchedConfig != null) ParseResult.Success(matchedConfig, stringResult.context)
            else ParseResult.Failure(stringResult.context)
        } else ParseResult.Failure(stringResult.context)
    }
}

/**
 * Builder for a [TypedConfigItemParser]
 *
 * @param T The type of the config item's value
 * @param A The args the parser will be a part of
 * @constructor Creates a typed config item parser builder
 *
 * @property type The KType for the config item
 */
class TypedConfigItemParserBuilder<T : Any, A : CommandArgs<A>> :
    ParserBuilder<Item<T>, A, TypedConfigItemParser<T, A>>() {
    lateinit var type: KType

    override fun checkValidity() {
        if (!::type.isInitialized) error("No type give for item arg.")
    }

    override fun build(): TypedConfigItemParser<T, A> {
        return TypedConfigItemParser(this)
    }
}
