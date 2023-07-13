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

/**
 * Adds a config item argument to these args
 *
 * @param A The type of the [CommandArgs] this parser is a part of
 * @param body The arg body
 * @receiver The builder for the parser
 * @return The built parser
 */
fun <A : CommandArgs<A>> CommandArgs<A>.configItem(
    body: ConfigItemParserBuilder<A>.() -> Unit,
): ConfigItemParser<A> =
    genericParser(::ConfigItemParserBuilder, body)

/**
 * Parser for a config item argument
 *
 * @param A The type of the args this parser is used by
 * @constructor Creates a config item parser
 *
 * @param builder The builder that defines this parser
 */
class ConfigItemParser<A : CommandArgs<A>>(builder: ConfigItemParserBuilder<A>) :
    ArgParser<Item<*>, A, ConfigItemParser<A>>(builder), KoinComponent {
    private val config: PluginConfig by inject()

    override fun typeName(): String = "ConfigItemType"

    private val internalStringParser = stringParser<A> {
        name = "CONFIG_INTERNAL"
        description = "Parses the string for the item."
    }

    override fun parseValue(context: ParseContext<A>): ParseResult<Item<*>, A> {
        val stringResult = internalStringParser.parseValue(context)

        return if (stringResult is ParseResult.Success) {
            val nextString = stringResult.result
            val matchedConfig = config[nextString]

            if (matchedConfig != null) {
                ParseResult.Success(matchedConfig, stringResult.context)
            } else {
                ParseResult.Failure(stringResult.context)
            }
        } else {
            ParseResult.Failure(stringResult.context)
        }
    }
}

/**
 * Builder for a [ConfigItemParser]
 *
 * @param A The args the parser will be a part of
 * @constructor Creates a config item parser builder
 */
class ConfigItemParserBuilder<A : CommandArgs<A>> : ParserBuilder<Item<*>, A, ConfigItemParser<A>>() {
    override fun checkValidity() = Unit

    override fun build(): ConfigItemParser<A> {
        return ConfigItemParser(this)
    }
}
