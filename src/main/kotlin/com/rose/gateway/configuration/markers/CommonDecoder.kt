package com.rose.gateway.configuration.markers

import com.rose.gateway.shared.configurations.canBe
import com.sksamuel.hoplite.ConfigFailure
import com.sksamuel.hoplite.ConfigResult
import com.sksamuel.hoplite.DecoderContext
import com.sksamuel.hoplite.Node
import com.sksamuel.hoplite.Undefined
import com.sksamuel.hoplite.decoder.NullHandlingDecoder
import com.sksamuel.hoplite.decoder.isInline
import com.sksamuel.hoplite.fp.ValidatedNel
import com.sksamuel.hoplite.fp.flatMap
import com.sksamuel.hoplite.fp.invalid
import com.sksamuel.hoplite.fp.sequence
import com.sksamuel.hoplite.fp.valid
import com.sksamuel.hoplite.isDefined
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

open class CommonDecoder : NullHandlingDecoder<Any> {
    override fun safeDecode(node: Node, type: KType, context: DecoderContext): ConfigResult<Any> {
        val klass = type.classifier as KClass<*>
        val constructor = klass.primaryConstructor

        if (constructor == null || constructor.parameters.isEmpty()) {
            return ConfigFailure.DataClassWithoutConstructor(klass).invalid()
        }

        data class Arg(
            val parameter: KParameter,
            val configName: String, // The config value name that was used.
            val value: Any?,
        )

        data class Constructor(
            val constructor: KFunction<Any>,
            val args: List<Arg>,
        )

        // Create a map of parameter to value. In the case of defaults, we skip the parameter completely.
        val args: ValidatedNel<ConfigFailure, List<Arg>> = constructor.parameters.mapNotNull { param ->
            var usedName = "<<undefined>>"

            // Use parameter mappers to retrieve alternative names, then try each one in turn.
            val names = context.paramMappers.flatMap { it.map(param) }
            val n = names.fold<String, Node>(Undefined) { n, name ->
                if (n.isDefined) n else {
                    usedName = name
                    node.atKey(name)
                }
            }

            context.usedPaths.add(n.path)

            when {
                // If we have no value for an optional parameter, we can skip it, and
                // Kotlin will use the default.
                param.isOptional && n is Undefined -> null
                else -> context.decoder(param)
                    .flatMap { it.decode(n, param.type, context) }
                    .map { Arg(param, usedName, it) }
                    .mapInvalid { ConfigFailure.ParamFailure(param, it) }
            }
        }.sequence()
        val result = args.map { Constructor(constructor, it) }

        return result.fold(
            // If invalid, we wrap in an error containing each individual error.
            { ConfigFailure.DataClassFieldErrors(it, type, node.pos).invalid() },
            { primaryConstructor ->
                construct(
                    type = type,
                    constructor = primaryConstructor.constructor,
                    args = primaryConstructor.args.associate { it.parameter to it.value },
                )
            }
        )
    }

    private fun <A> construct(
        type: KType,
        constructor: KFunction<A>,
        args: Map<KParameter, Any?>
    ): ConfigResult<A> {
        return try {
            constructor.callBy(args).valid()
        } catch (e: InvocationTargetException) {
            ConfigFailure.InvalidConstructorParameters(type, constructor, args, e.cause ?: e).invalid()
        } catch (e: IllegalArgumentException) {
            ConfigFailure.InvalidConstructorParameters(type, constructor, args, e.cause ?: e).invalid()
        }
    }

    override fun supports(type: KType): Boolean {
        return when (type.classifier is KClass<*>) {
            true -> {
                val kClass = type.classifier as KClass<*>
                (!kClass.isData && !kClass.isSealed && !kClass.isInline() && kClass canBe ConfigObject::class)
            }
            false -> false
        }
    }
}
