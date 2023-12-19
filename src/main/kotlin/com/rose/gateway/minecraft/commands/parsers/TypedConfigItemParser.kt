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
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Parser for a typed config item argument
 *
 * @param T The type of the config item's value
 * @param A The type of the args this parser is used by
 * @constructor Creates a typed config item parser
 *
 * @param builder The builder that defines this parser
 */
class TypedConfigItemParser<T : Any>(builder: TypedConfigItemParserBuilder<T>) :
    ArgParser<Item<T>, TypedConfigItemParser<T>, TypedConfigItemParserBuilder<T>>(builder), KoinComponent {
    private val config: PluginConfig by inject()

    override fun typeName(): String = "ConfigItem"

    private val internalStringParser = stringParser {
        name = "CONFIG_INTERNAL"
        description = "Parses the string for the item."
    }

    override fun parseValue(context: ParseContext): ParseResult<Item<T>> {
        val stringResult = internalStringParser.parseValue(context)

        return if (stringResult is ParseResult.Success) {
            val nextString = stringResult.value
            val matchedConfig = config.get<T>(builder.type, nextString)

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
 * Builder for a [TypedConfigItemParser]
 *
 * @param T The type of the config item's value
 * @param A The args the parser will be a part of
 * @constructor Creates a typed config item parser builder
 */
class TypedConfigItemParserBuilder<T : Any>(valueType: KType) :
    ParserBuilder<Item<T>, TypedConfigItemParser<T>, TypedConfigItemParserBuilder<T>>(
        "TypedConfigItem",
        "A config item with a specific type",
    ) {
    companion object {
        inline fun <reified T : Any> constructor(): () -> TypedConfigItemParserBuilder<T> {
            return {
                TypedConfigItemParserBuilder(typeOf<T>())
            }
        }
    }

    /**
     * The KType for the config item
     */
    var type: KType = valueType

    init {
        completer = ConfigCompleter.configItemsWithType(type)
    }

    override fun build(): TypedConfigItemParser<T> {
        return TypedConfigItemParser(this)
    }
}
