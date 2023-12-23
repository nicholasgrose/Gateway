package com.rose.gateway.minecraft.commands.framework.args

import com.rose.gateway.config.Item
import com.rose.gateway.minecraft.commands.framework.CommandBuilder
import com.rose.gateway.minecraft.commands.framework.data.context.ArgsContext
import com.rose.gateway.minecraft.commands.framework.data.context.CommandExecuteContext
import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
import com.rose.gateway.minecraft.commands.framework.data.definition.CommandExecutionResult
import com.rose.gateway.minecraft.commands.parsers.BooleanParser
import com.rose.gateway.minecraft.commands.parsers.BooleanParserBuilder
import com.rose.gateway.minecraft.commands.parsers.ConfigItemParser
import com.rose.gateway.minecraft.commands.parsers.ConfigItemParserBuilder
import com.rose.gateway.minecraft.commands.parsers.IntParser
import com.rose.gateway.minecraft.commands.parsers.IntParserBuilder
import com.rose.gateway.minecraft.commands.parsers.ListParser
import com.rose.gateway.minecraft.commands.parsers.ListParserBuilder
import com.rose.gateway.minecraft.commands.parsers.StringParser
import com.rose.gateway.minecraft.commands.parsers.StringParserBuilder
import com.rose.gateway.minecraft.commands.parsers.TypedConfigItemParser
import com.rose.gateway.minecraft.commands.parsers.TypedConfigItemParserBuilder

/**
 * Type alias for an ArgParser<Boolean, BooleanParser, BooleanParserBuilder>
 */
typealias BooleanArg = ArgParser<Boolean, BooleanParser, BooleanParserBuilder>

/**
 * A typealias for [ArgParser] with type arguments [Int, IntParser, IntParserBuilder].
 *
 * This typealias is used to simplify the declaration of arg parsers for integers.
 */
typealias IntArg = ArgParser<Int, IntParser, IntParserBuilder>

/**
 * The `StringArg` typealias represents an argument parser specifically designed to parse string arguments.
 *
 * It is a typealias for `ArgParser<String, StringParser, StringParserBuilder>`, which supports parsing string
 * arguments using `StringParser` and building string arguments using `StringParserBuilder`.
 *
 * @see ArgParser
 * @see StringParser
 * @see StringParserBuilder
 */
typealias StringArg = ArgParser<String, StringParser, StringParserBuilder>

/**
 *
 */
fun CommandBuilder.stringArg(
    parserBuilderBody: StringParserBuilder.() -> Unit,
    commandBuilderBody: CommandBuilder.(StringArg) -> Unit,
) {
    addExecutorForArg(::StringParserBuilder, parserBuilderBody, commandBuilderBody)
}

/**
 * Adds an integer argument to the command builder.
 *
 * @param parserBuilderBody The configuration for the integer parser builder
 * @param commandBuilderBody The command to execute when this argument is provided
 * @receiver The command builder to add the argument to
 */
fun CommandBuilder.intArg(
    parserBuilderBody: IntParserBuilder.() -> Unit,
    commandBuilderBody: CommandBuilder.(IntArg) -> Unit,
) {
    addExecutorForArg(::IntParserBuilder, parserBuilderBody, commandBuilderBody)
}

/**
 * Adds a boolean argument to the command builder.
 *
 * @param parserBuilderBody The builder body for the BooleanParserBuilder
 * @param commandBuilderBody The builder body for the CommandBuilder
 */
fun CommandBuilder.booleanArg(
    parserBuilderBody: BooleanParserBuilder.() -> Unit,
    commandBuilderBody: CommandBuilder.(BooleanArg) -> Unit,
) {
    addExecutorForArg(::BooleanParserBuilder, parserBuilderBody, commandBuilderBody)
}

/**
 * Represents a type alias for a list of arguments with String value type.
 *
 * @param <ListArg> the type of the list argument
 * @param <StringParser> the type of the string parser
 * @param <StringParserBuilder> the type of the string parser builder
 */
typealias StringListArg = ListArg<String, StringParser, StringParserBuilder>

/**
 * A builder for creating a string list parser.
 *
 * @param <R> The type of the result that the parser produces.
 * @param <P> The type of the parser used to parse individual elements.
 * @param <B> The type of the builder used to configure the parser.
 */
typealias StringListParserBuilder = ListParserBuilder<String, StringParser, StringParserBuilder>

/**
 * typealias statement for a list argument parser
 *
 * @param T The type of the list elements
 * @param P The type of the parser for the list elements
 * @param B The type of the builder for the list parser
 */
typealias ListArg<T, P, B> = ArgParser<List<T>, ListParser<T, P, B>, ListParserBuilder<T, P, B>>

/**
 * Adds a string list argument to the command.
 *
 * @param parserBuilderBody A lambda function containing the configuration of the string list parser builder.
 * @param commandBuilderBody A lambda function containing the configuration of the command builder.
 */
fun CommandBuilder.stringListArg(
    parserBuilderBody: StringListParserBuilder.() -> Unit,
    commandBuilderBody: CommandBuilder.(StringListArg) -> Unit,
) {
    listArg(parserBuilderBody, commandBuilderBody)
}

/**
 * Adds a list argument to the command builder.
 *
 * @param parserBuilderBody A lambda function that configures the list parser builder
 * @param commandBuilderBody A lambda function that defines how the list argument should be handled
 * @param T The type of the list elements
 * @param P The type of the arg parser for list elements
 * @param B The type of the parser builder for list elements
 */
fun <T, P, B> CommandBuilder.listArg(
    parserBuilderBody: ListParserBuilder<T, P, B>.() -> Unit,
    commandBuilderBody: CommandBuilder.(ListArg<T, P, B>) -> Unit,
) where T : Any, P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
    addExecutorForArg(::ListParserBuilder, parserBuilderBody, commandBuilderBody)
}

/**
 * A typealias for the ConfigItemArg parser
 */
typealias ConfigItemArg = ArgParser<Item<*>, ConfigItemParser, ConfigItemParserBuilder>

/**
 * Configures an argument for the command.
 * This method takes in two lambda expressions: `parserBuilderBody` and `commandBuilderBody`.
 *
 * @param parserBuilderBody The lambda expression that configures the [ConfigItemParserBuilder] for the argument.
 * @param commandBuilderBody The lambda expression that configures the [CommandBuilder] for the argument.
 * @receiver The command builder to which the argument is being added.
 */
fun CommandBuilder.configArg(
    parserBuilderBody: ConfigItemParserBuilder.() -> Unit,
    commandBuilderBody: CommandBuilder.(ConfigItemArg) -> Unit,
) {
    addExecutorForArg(::ConfigItemParserBuilder, parserBuilderBody, commandBuilderBody)
}

/**
 * Represents a typed configuration item argument.
 *
 * @param T the type of the configuration item
 */
typealias TypedConfigItemArg<T> = ArgParser<Item<T>, TypedConfigItemParser<T>, TypedConfigItemParserBuilder<T>>

/**
 * Adds a typed configuration argument to the command builder.
 *
 * @param T The type of the configuration item
 * @param parserBuilderBody The body of the parser builder, used to configure the parser
 * @param commandBuilderBody The body of the command builder, used to configure the command
 * @receiver The command builder
 */
inline fun <reified T : Any> CommandBuilder.typedConfigArg(
    parserBuilderBody: TypedConfigItemParserBuilder<T>.() -> Unit,
    commandBuilderBody: CommandBuilder.(TypedConfigItemArg<T>) -> Unit,
) {
    addExecutorForArg(TypedConfigItemParserBuilder.constructor<T>(), parserBuilderBody, commandBuilderBody)
}

/**
 * Add an executor for the given argument parser
 *
 * @param T The type of the result from parsing
 * @param P The type of the argument parser
 * @param B The type of the parser builder
 * @param parserBuilderConstructor A function that constructs the parser builder
 * @param parserBuilderBody A function that configures the parser builder
 * @param commandBuilderBody A function that configures the command builder
 */
inline fun <T, P, B> CommandBuilder.addExecutorForArg(
    parserBuilderConstructor: () -> B,
    parserBuilderBody: B.() -> Unit,
    commandBuilderBody: CommandBuilder.(ArgParser<T, P, B>) -> Unit,
) where P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
    val parserBuilder = parserBuilderConstructor().apply(parserBuilderBody)
    val parser = parserBuilder.build()

    val commandBuilder = CommandBuilder(parser.name())
    commandBuilderBody(commandBuilder, parser)

    val command = commandBuilder.build()

    parserBuilder.apply {
        completer = { context ->
            val resultContext = context.resultOfOrDefault(parser).context

            if (resultContext.currentIndex < context.args.raw.size) {
                command.parseAndComplete(
                    TabCompleteContext(
                        context.command,
                        context.args,
                        context.bukkit,
                    ),
                )
            } else {
                completer(context)
            }
        }
    }

    executes(parser) { context ->
        val resultContext = context.resultOfOrDefault(parser).context
        val args = context.args

        command.parseAndExecute(
            CommandExecuteContext(
                context.command,
                ArgsContext(
                    args.raw.subList(resultContext.currentIndex, args.raw.size),
                    args.parsed,
                ),
                context.bukkit,
            ),
        ) is CommandExecutionResult.Successful
    }
}
