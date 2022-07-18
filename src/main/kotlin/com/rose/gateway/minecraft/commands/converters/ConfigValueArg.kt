package com.rose.gateway.minecraft.commands.converters

import com.rose.gateway.configuration.Item
import com.rose.gateway.configuration.PluginConfiguration
import com.rose.gateway.minecraft.commands.framework.runner.ArgBuilder
import com.rose.gateway.minecraft.commands.framework.runner.ParseContext
import com.rose.gateway.minecraft.commands.framework.runner.ParseResult
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArg
import com.rose.gateway.minecraft.commands.framework.runner.RunnerArguments
import com.rose.gateway.shared.configurations.canBe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

fun <A : RunnerArguments<A>> RunnerArguments<A>.configValue(
    body: ConfigValueArgBuilder<A>.() -> Unit
): ConfigValueArg<A> =
    genericParser(::ConfigValueArgBuilder, body)

class ConfigValueArg<A : RunnerArguments<A>>(val builder: ConfigValueArgBuilder<A>) :
    RunnerArg<Any?, A, ConfigValueArg<A>>(builder), KoinComponent {
    val config: PluginConfiguration by inject()

    override fun typeName(): String = "ConfigValue"

    override fun parseValue(context: ParseContext<A>): ParseResult<Any?, A> {
        val configItem = builder.itemArg.getter.call()
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
        val parser = parserFor(configItem) ?: return failedParseResult(context)
        val parseResult = parser.parseValidValue(context)

        return ParseResult(
            succeeded = parseResult.succeeded,
            result = parseResult.result,
            context = parseResult.context
        )
    }

    private fun <T> parserFor(configItem: Item<T>): RunnerArg<T, A, *>? {
        val configClass = configItem.typeClass()

        @Suppress("UNCHECKED_CAST")
        return if (configClass canBe List::class) {
            val configType = configClass.typeParameters.first().upperBounds.first().classifier?.run {
                if (this is KClass<*>) this else null
            }
            val listParser = when {
                configType == null -> null
                configType canBe Boolean::class -> {
                    listArg<Boolean, A, BooleanArg<A>> {
                        name = builder.name
                        description = builder.description
                        argType = booleanArg<A> {
                            name = builder.name
                            description = builder.description
                        }
                    }
                }
                configType canBe Int::class -> {
                    listArg<Int, A, IntArg<A>> {
                        name = builder.name
                        description = builder.description
                        argType = intArg {
                            name = builder.name
                            description = builder.description
                        }
                    }
                }
                configType canBe String::class -> {
                    listArg<String, A, StringArg<A>> {
                        name = builder.name
                        description = builder.description
                        argType = stringArg {
                            name = builder.name
                            description = builder.description
                        }
                    }
                }
                else -> null
            }

            if (listParser == null) null else listParser as RunnerArg<T, A, *>?
        } else parserMap()[configClass] as RunnerArg<T, A, *>?
    }

    private fun parserMap(): Map<KClass<out Any>, RunnerArg<out Any, A, *>> = mapOf(
        Boolean::class to booleanArg {
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
