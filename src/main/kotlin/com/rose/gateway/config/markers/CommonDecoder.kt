package com.rose.gateway.config.markers

import com.rose.gateway.shared.reflection.canBe
import com.sksamuel.hoplite.ConfigResult
import com.sksamuel.hoplite.DecoderContext
import com.sksamuel.hoplite.Node
import com.sksamuel.hoplite.decoder.DataClassDecoder
import com.sksamuel.hoplite.decoder.NullHandlingDecoder
import com.sksamuel.hoplite.decoder.isInline
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * A decoder implementation for Hoplite to use when decoding the Gateway config schema
 * This is similar to the [DataClassDecoder] except that it only handles [ConfigObject]s and ignores data classes
 *
 * @constructor Create a Common decoder
 */
open class CommonDecoder : NullHandlingDecoder<Any> {
    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object {
        /**
         * The data class decoder this decoder uses internally
         */
        private val DECODER = DataClassDecoder()
    }

    override fun safeDecode(
        node: Node,
        type: KType,
        context: DecoderContext,
    ): ConfigResult<Any> = DECODER.safeDecode(node, type, context)

    /**
     * Checks if the decoder supports the given type.
     *
     * @param type The type to check.
     * @return True if the type is supported, false otherwise.
     */
    override fun supports(type: KType): Boolean {
        val classifier = type.classifier
        return classifier is KClass<*> && isSupportedClassifier(classifier)
    }

    /**
     * Check if the classifier is not a data class, sealed class, inline class
     * and can be a ConfigObject.
     *
     * @param classifier The class we want to check
     * @return true if it passes all conditions, false otherwise
     */
    fun isSupportedClassifier(classifier: KClass<*>): Boolean =
        !classifier.isData && !classifier.isSealed && !classifier.isInline() && classifier canBe ConfigObject::class
}
