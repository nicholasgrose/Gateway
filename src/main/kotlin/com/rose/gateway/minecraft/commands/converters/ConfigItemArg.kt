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

fun <A : RunnerArguments<A>> RunnerArguments<A>.configItem(body: ConfigItemArgBuilder<A>.() -> Unit): ConfigItemArg<A> =
    genericParser(::ConfigItemArgBuilder, body)

class ConfigItemArg<A : RunnerArguments<A>>(builder: ConfigItemArgBuilder<A>) :
    RunnerArg<Item<*>, A, ConfigItemArg<A>>(builder), KoinComponent {
    val config: PluginConfiguration by inject()

    override fun typeName(): String = String::class.simpleName.toString()

    override fun parseValue(context: ParseContext<A>): ParseResult<Item<*>, A> {
        val args = context.arguments
        val currentIndex = context.currentIndex
        val configName = context.arguments.rawArguments.getOrNull(currentIndex)
            ?: return ParseResult(
                succeeded = false,
                context = ParseContext(
                    arguments = args,
                    currentIndex = currentIndex + 1
                ),
                result = null
            )

        val matchingConfigItems = config.stringMap.matchingOrAllStrings(configName)

        return if (matchingConfigItems.size == 1) {
            val item = config[matchingConfigItems.first()]

            ParseResult(
                succeeded = item != null,
                context = ParseContext(
                    arguments = args,
                    currentIndex = currentIndex + 1
                ),
                result = item
            )
        } else ParseResult(
            succeeded = false,
            context = ParseContext(
                arguments = args,
                currentIndex = currentIndex + 1
            ),
            result = null
        )
    }
}

class ConfigItemArgBuilder<A : RunnerArguments<A>> : ArgBuilder<Item<*>, A, ConfigItemArg<A>>() {
    override fun checkValidity() = Unit

    override fun build(): ConfigItemArg<A> {
        return ConfigItemArg(this)
    }
}
