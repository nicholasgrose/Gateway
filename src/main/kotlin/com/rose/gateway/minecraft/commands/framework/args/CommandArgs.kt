// package com.rose.gateway.minecraft.commands.framework.args
//
// import com.rose.gateway.minecraft.commands.framework.data.context.FrameworkContext
// import com.rose.gateway.minecraft.commands.framework.data.context.ParseContext
// import com.rose.gateway.minecraft.commands.framework.data.context.ParserSpecificContext
// import com.rose.gateway.minecraft.commands.framework.data.context.TabCompleteContext
// import com.rose.gateway.minecraft.commands.framework.data.executor.ArgsReference
// import com.rose.gateway.minecraft.commands.parsers.UnitParser
//
// /**
// * A set of arguments for a command
// *
// * @param A The type of these arguments
// * @constructor Create empty command arguments
// */
// @Suppress("TooManyFunctions")
// open class CommandArgs {
//    /**
//     * The raw, unparsed command arguments that came in
//     */
//    var rawArguments: List<String> = listOf()
//
//    /**
//     * The parsers that parse the raw arguments for these arguments
//     */
//    val parsers: MutableList<ArgParser<*, *>> = mutableListOf()
//
//    /**
//     * The results from parsing the raw arguments with all stored parsers
//     */
//    var parserResults: Map<ArgParser<*, *>, ParseResult.Success<*>> = mapOf()
//
//    /**
//     * Fills in this argument's [parserResults] for a set of raw arguments
//     *
//     * @param rawArgs The raw arguments to parse for the final result
//     */
//    fun parseArguments(argsRef: ArgsReference<A>, context: FrameworkContext<*>): A {
//        rawArguments = context.args.raw
//        fillFinalParseResults(
//            ParseContext(
//                context.command,
//                context.args,
//                context.bukkit,
//                ParserSpecificContext(
//                    argsRef,
//                    UnitParser()
//                ),
//                0
//            )
//        )
//
//        @Suppress("UNCHECKED_CAST")
//        return this as A
//    }
//
//    /**
//     * This constructs the [parserResults] by running all parsers on the [rawArguments].
//     * It fills [parserResults] with intermediate values each time a parser succeeds.
//     *
//     * @return The final result of running all parsers
//     */
//    private fun fillFinalParseResults(context: ParseContext<A>) {
//        @Suppress("UNCHECKED_CAST")
//        var currentContext = context
//        val resultMap = mutableMapOf<ArgParser<*, A, *>, ParseResult.Success<*, A>>()
//        parserResults = resultMap
//
//        for (parser in parsers) {
//            val result = parser.parseValidValue(currentContext)
//
//            currentContext = result.context
//
//            if (result is ParseResult.Success) {
//                resultMap[parser] = result
//                parserResults = resultMap
//            } else {
//                parserResults = resultMap
//                return
//            }
//        }
//
//        parserResults = resultMap
//    }
//
//    /**
//     * Determines which parse result was the last one to finish successfully
//     *
//     * @return The most recently successful parse result or null if none could be found
//     */
//    private fun lastSuccessfulResult(): ParseResult<*, A>? {
//        val finalResult = parserResults
//
//        val lastSuccessfulArg = parsers.lastOrNull { wasSuccessful(it) } ?: return null
//
//        return finalResult[lastSuccessfulArg]
//    }
//
//    /**
//     * This determines whether these arguments are valid.
//     * Validity is defined as all parsers having parsed successfully without leaving any arguments unused.
//     *
//     * @param allowUnusedArgs Whether unused arguments should be considered a reason to fail
//     * @return Whether these arguments are valid
//     */
//    fun valid(allowUnusedArgs: Boolean = false): Boolean {
//        if (!allowUnusedArgs && hasUnusedArgs()) {
//            return false
//        }
//
//        return parsers.all { wasSuccessful(it) }
//    }
//
//    /**
//     * Determines whether any raw arguments are unused by parsers
//     *
//     * @return Whether there are any unused args
//     */
//    private fun hasUnusedArgs(): Boolean = remainingArgCount() > 0
//
//    /**
//     * How many raw args are not used by any parsers
//     *
//     * @return The number of unused args remaining
//     */
//    private fun remainingArgCount(): Int = rawArguments.size - lastIndex()
//
//    /**
//     * The index of the context of the furthest parsed result
//     *
//     * @return The next index or 0 if no parsing succeeded
//     */
//    fun lastIndex(): Int = lastSuccessfulResult()?.context?.currentIndex ?: 0
//
//    /**
//     * Determines whether a particular arg was successfully parsed
//     *
//     * @param arg The arg to check the parse status of
//     * @return Whether a parsed value exists for the arg
//     */
//    fun wasSuccessful(arg: ArgParser<*, *, *>): Boolean = parserResults.containsKey(arg)
//
//    /**
//     * Gives the remaining arguments that haven't been consumed in parsing
//     *
//     * @return The list of remaining arguments
//     */
//    fun remainingArgs(): List<String> = rawArguments.subList(lastIndex(), rawArguments.size)
//
//    /**
//     * Determines the number of raw arguments that have been parsed
//     *
//     * @return the number of raw arguments that have been parsed
//     */
//    fun argsParsed(): Int = lastSuccessfulResult()?.context?.currentIndex ?: 0
//
//    /**
//     * Determines all possible valid usages for these args
//     *
//     * @return All possible usages for these args
//     */
//    fun usages(): List<String> {
//        val parserUsages = parsers.map { parser ->
//            parser.generateUsages()
//        }
//        var allUsages = listOf<String>()
//
//        for (usageList in parserUsages) {
//            if (usageList.isEmpty()) continue
//
//            allUsages = if (allUsages.isEmpty()) {
//                usageList.toList()
//            } else {
//                usageList.flatMap { usage ->
//                    allUsages.map { existingUsage -> "$existingUsage $usage" }
//                }.toList()
//            }
//        }
//
//        return allUsages
//    }
//
//    /**
//     * Determines the valid tab completions for these args in the given context
//     *
//     * @param context The context to complete these args within
//     * @return All completions this arg has in the given context
//     */
//    fun completions(argsRef: ArgsReference<A>, context: TabCompleteContext<A>): List<String> {
//        val nextArg = parsers.firstOrNull {
//            !(wasSuccessful(it))
//        } ?: parsers.lastOrNull()
//
//        return when {
//            remainingArgCount() > 1 -> listOf()
//            else -> nextArg?.completions(
//                TabCompleteContext(
//                    context.command,
//                    context.args,
//                    context.bukkit,
//                    ParserSpecificContext(
//                        argsRef,
//                        nextArg
//                    )
//                )
//            ) ?: listOf()
//        }
//    }
// }
