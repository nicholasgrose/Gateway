package com.rose.gateway.shared.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A class that provides functions that map from [SerializedType] to [SurrogateType] and vice versa.
 *
 * @param SerializedType The target type for conversion.
 * @param SurrogateType The surrogate type for conversion.
 * @constructor Create a surrogate converter.
 *
 * @see SurrogateBasedSerializer
 */
interface SurrogateConverter<SerializedType, SurrogateType> {
    /**
     * Maps a serialized value [SerializedType] into a surrogate [SurrogateType].
     *
     * @param base The original value of the type.
     * @return The surrogate version of the serialized value.
     */
    fun fromBase(base: SerializedType): SurrogateType

    /**
     * Maps a surrogate [SurrogateType] into a serialized value [SerializedType].
     *
     * @param surrogate The surrogate version of the serialized value.
     * @return The serialized version of the surrogate value.
     */
    fun toBase(surrogate: SurrogateType): SerializedType
}

/**
 * A serializer that can act as a surrogate for another type.
 * That is, it can be a serializer for a type [SerializedType] that uses a secondary type [SurrogateType]
 * as an intermediary type when serializing and deserializing for [SerializedType].
 *
 * @param SerializedType The type this serializer can be a serializer for.
 * @param SurrogateType The secondary, intermediate type this serializer uses in place of T.
 * @property surrogateSerializer The serializer that handles the surrogate type [SurrogateType].
 * @property converter A converter that can map between [SerializedType] and [SurrogateType].
 * @constructor Create a surrogate-based serializer.
 *
 * @see KSerializer
 * @see SurrogateConverter
 */
open class SurrogateBasedSerializer<SerializedType : Any, SurrogateType : Any>(
    private val surrogateSerializer: KSerializer<SurrogateType>,
    private val converter: SurrogateConverter<SerializedType, SurrogateType>
) : KSerializer<SerializedType> {
    override val descriptor = surrogateSerializer.descriptor

    override fun deserialize(decoder: Decoder): SerializedType {
        val surrogate: SurrogateType = decoder.decodeSerializableValue(surrogateSerializer)

        return converter.toBase(surrogate)
    }

    override fun serialize(encoder: Encoder, value: SerializedType) {
        val surrogate = converter.fromBase(value)

        encoder.encodeSerializableValue(surrogateSerializer, surrogate)
    }
}
