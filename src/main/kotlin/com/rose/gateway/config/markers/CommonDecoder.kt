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

open class CommonDecoder : NullHandlingDecoder<Any> {
    companion object {
        val DECODER = DataClassDecoder()
    }

    override fun safeDecode(node: Node, type: KType, context: DecoderContext): ConfigResult<Any> {
        return DECODER.safeDecode(node, type, context)
    }

    override fun supports(type: KType): Boolean {
        val classifier = type.classifier

        return if (classifier is KClass<*>) (
            !classifier.isData &&
                !classifier.isSealed &&
                !classifier.isInline() &&
                classifier canBe ConfigObject::class
            )
        else false
    }
}
