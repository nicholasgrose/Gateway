package com.rose.gateway.shared.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A class that provides functions that map from [TypeToSerialize] to [SurrogateType] and vice versa
 *
 * @param TypeToSerialize The target type for conversion
 * @param SurrogateType The surrogate type for conversion
 * @constructor Create a surrogate converter
 *
 * @see SurrogateBasedSerializer
 */
interface SurrogateConverter<TypeToSerialize, SurrogateType> {
    /**
     * Maps a serialized value [TypeToSerialize] into a surrogate [SurrogateType]
     *
     * @param base The original value of the type
     * @return The surrogate version of the serialized value
     */
    fun fromBase(base: TypeToSerialize): SurrogateType

    /**
     * Maps a surrogate [SurrogateType] into a serialized value [TypeToSerialize]
     *
     * @param surrogate The surrogate version of the serialized value
     * @return The serialized version of the surrogate value
     */
    fun toBase(surrogate: SurrogateType): TypeToSerialize
}

/**
 * A serializer that takes [TypeToSerialize], converts it to [SurrogateType] and then serializes it
 * It also provides the reverse operation
 *
 * @param TypeToSerialize The type this serializer can be a serializer for
 * @param SurrogateType The secondary, intermediate type this serializer uses in place of T
 * @property surrogateSerializer The serializer that handles the surrogate type [SurrogateType]
 * @property converter A converter that can map between [TypeToSerialize] and [SurrogateType]
 * @constructor Create a surrogate-based serializer
 *
 * @see KSerializer
 * @see SurrogateConverter
 */
open class SurrogateBasedSerializer<TypeToSerialize : Any, SurrogateType : Any>(
    private val surrogateSerializer: KSerializer<SurrogateType>,
    private val converter: SurrogateConverter<TypeToSerialize, SurrogateType>,
) : KSerializer<TypeToSerialize> {
    override val descriptor = surrogateSerializer.descriptor

    override fun deserialize(decoder: Decoder): TypeToSerialize {
        val surrogate: SurrogateType = decoder.decodeSerializableValue(surrogateSerializer)

        return converter.toBase(surrogate)
    }

    override fun serialize(encoder: Encoder, value: TypeToSerialize) {
        val surrogate = converter.fromBase(value)

        encoder.encodeSerializableValue(surrogateSerializer, surrogate)
    }
}
