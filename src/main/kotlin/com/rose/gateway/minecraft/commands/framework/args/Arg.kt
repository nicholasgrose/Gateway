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

typealias BooleanArg = ArgParser<Boolean, BooleanParser, BooleanParserBuilder>
typealias IntArg = ArgParser<Int, IntParser, IntParserBuilder>
typealias StringArg = ArgParser<String, StringParser, StringParserBuilder>

fun CommandBuilder.stringArg(
    parserBuilderBody: StringParserBuilder.() -> Unit,
    commandBuilderBody: CommandBuilder.(StringArg) -> Unit,
) {
    addExecutorForArg(::StringParserBuilder, parserBuilderBody, commandBuilderBody)
}

fun CommandBuilder.intArg(
    parserBuilderBody: IntParserBuilder.() -> Unit,
    commandBuilderBody: CommandBuilder.(IntArg) -> Unit,
) {
    addExecutorForArg(::IntParserBuilder, parserBuilderBody, commandBuilderBody)
}

fun CommandBuilder.booleanArg(
    parserBuilderBody: BooleanParserBuilder.() -> Unit,
    commandBuilderBody: CommandBuilder.(BooleanArg) -> Unit,
) {
    addExecutorForArg(::BooleanParserBuilder, parserBuilderBody, commandBuilderBody)
}

typealias StringListArg = ListArg<String, StringParser, StringParserBuilder>
typealias StringListParserBuilder = ListParserBuilder<String, StringParser, StringParserBuilder>

typealias ListArg<T, P, B> = ArgParser<List<T>, ListParser<T, P, B>, ListParserBuilder<T, P, B>>

fun CommandBuilder.stringListArg(
    parserBuilderBody: StringListParserBuilder.() -> Unit,
    commandBuilderBody: CommandBuilder.(StringListArg) -> Unit,
) {
    listArg(parserBuilderBody, commandBuilderBody)
}

fun <T, P, B> CommandBuilder.listArg(
    parserBuilderBody: ListParserBuilder<T, P, B>.() -> Unit,
    commandBuilderBody: CommandBuilder.(ListArg<T, P, B>) -> Unit,
) where T : Any, P : ArgParser<T, P, B>, B : ParserBuilder<T, P, B> {
    addExecutorForArg(::ListParserBuilder, parserBuilderBody, commandBuilderBody)
}

typealias ConfigItemArg = ArgParser<Item<*>, ConfigItemParser, ConfigItemParserBuilder>

fun CommandBuilder.configArg(
    parserBuilderBody: ConfigItemParserBuilder.() -> Unit,
    commandBuilderBody: CommandBuilder.(ConfigItemArg) -> Unit,
) {
    addExecutorForArg(::ConfigItemParserBuilder, parserBuilderBody, commandBuilderBody)
}

typealias TypedConfigItemArg<T> = ArgParser<Item<T>, TypedConfigItemParser<T>, TypedConfigItemParserBuilder<T>>

inline fun <reified T : Any> CommandBuilder.typedConfigArg(
    parserBuilderBody: TypedConfigItemParserBuilder<T>.() -> Unit,
    commandBuilderBody: CommandBuilder.(TypedConfigItemArg<T>) -> Unit,
) {
    addExecutorForArg(TypedConfigItemParserBuilder.constructor<T>(), parserBuilderBody, commandBuilderBody)
}

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
