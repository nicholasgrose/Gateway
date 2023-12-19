package com.rose.gateway.minecraft.commands.parsers

import com.rose.gateway.config.Item
import com.rose.gateway.config.PluginConfig
import com.rose.gateway.minecraft.commands.completers.ConfigCompleter
import com.rose.gateway.minecraft.commands.framework.args.ArgParser
import com.rose.gateway.minecraft.commands.framework.args.ParseResult
import com.rose.gateway.minecraft.commands.framework.args.ParserBuilder
import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Parser for a config item argument
 *
 * @param A The type of the args this parser is used by
 * @constructor Creates a config item parser
 *
 * @param builder The builder that defines this parser
 */
class ConfigItemParser(builder: ConfigItemParserBuilder) :
    ArgParser<Item<*>, ConfigItemParser, ConfigItemParserBuilder>(builder), KoinComponent {
    private val config: PluginConfig by inject()

    override fun typeName(): String = "ConfigItemType"

    private val internalStringParser = stringParser {
        name = "CONFIG_INTERNAL"
        description = "Parses the string for the item."
    }

    override fun parseValue(context: ParseContext): ParseResult<Item<*>> {
        val stringResult = internalStringParser.parseValue(context)

        return if (stringResult is ParseResult.Success) {
            val nextString = stringResult.value
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
class ConfigItemParserBuilder : ParserBuilder<Item<*>, ConfigItemParser, ConfigItemParserBuilder>(
    "ConfigItem",
    "A path to a particular item in the config",
) {
    init {
        completer = ConfigCompleter.configStrings()
    }

    override fun build(): ConfigItemParser {
        return ConfigItemParser(this)
    }
}
