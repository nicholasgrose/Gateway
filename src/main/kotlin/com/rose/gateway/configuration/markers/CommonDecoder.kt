package com.rose.gateway.configuration.markers

import com.rose.gateway.shared.configurations.canBe
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
        private val DECODER = DataClassDecoder()
    }

    override fun safeDecode(node: Node, type: KType, context: DecoderContext): ConfigResult<Any> {
        return DECODER.safeDecode(node, type, context)
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
