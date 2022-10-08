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

fun <A : CommandArgs<A>> CommandArgs<A>.configItem(
    body: ConfigItemParserBuilder<A>.() -> Unit
): ConfigItemParser<A> =
    genericParser(::ConfigItemParserBuilder, body)

class ConfigItemParser<A : CommandArgs<A>>(builder: ConfigItemParserBuilder<A>) :
    ArgParser<Item<*>, A, ConfigItemParser<A>>(builder), KoinComponent {
    val config: PluginConfig by inject()

    override fun typeName(): String = "ConfigItemType"

    private val internalStringParser = stringParser<A> {
        name = "CONFIG_INTERNAL"
        description = "Parses the string for the item."
    }

    override fun parseValue(context: ParseContext<A>): ParseResult<Item<*>, A> {
        val stringResult = internalStringParser.parseValue(context)

        return if (stringResult.succeeded && stringResult.result != null) {
            val nextString = stringResult.result
            val matchedConfig = config[nextString]

            ParseResult(
                succeeded = matchedConfig != null,
                result = matchedConfig,
                context = stringResult.context
            )
        } else failedParseResult(stringResult.context)
    }
}

class ConfigItemParserBuilder<A : CommandArgs<A>> : ParserBuilder<Item<*>, A, ConfigItemParser<A>>() {
    override fun checkValidity() = Unit

    override fun build(): ConfigItemParser<A> {
        return ConfigItemParser(this)
    }
}
