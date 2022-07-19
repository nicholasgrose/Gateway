package com.rose.gateway.configuration.markers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

interface SurrogateConverter<T, S> {
    fun fromBase(base: T): S
    fun toBase(surrogate: S): T
}

open class SurrogateBasedSerializer<T : Any, S : Any>(
    private val surrogateSerializer: KSerializer<S>,
    private val converter: SurrogateConverter<T, S>
) : KSerializer<T> {
    override val descriptor = surrogateSerializer.descriptor

    override fun deserialize(decoder: Decoder): T {
        val surrogate: S = decoder.decodeSerializableValue(surrogateSerializer)

        return converter.toBase(surrogate)
    }

    override fun serialize(encoder: Encoder, value: T) {
        val surrogate = converter.fromBase(value)

        encoder.encodeSerializableValue(surrogateSerializer, surrogate)
    }
}
