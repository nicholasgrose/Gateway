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

class ConfigItemArg<A : RunnerArguments<A>>(builder: ConfigItemArgBuilder<A>) :
    RunnerArg<Item<*>, A, ConfigItemArg<A>>(builder), KoinComponent {
    val config: PluginConfiguration by inject()

    override fun typeName(): String = String::class.simpleName.toString()

    private val internalStringParser = stringArg<A> {
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

class ConfigItemArgBuilder<A : RunnerArguments<A>> : ArgBuilder<Item<*>, A, ConfigItemArg<A>>() {
    override fun checkValidity() = Unit

    override fun build(): ConfigItemArg<A> {
        return ConfigItemArg(this)
    }
}
