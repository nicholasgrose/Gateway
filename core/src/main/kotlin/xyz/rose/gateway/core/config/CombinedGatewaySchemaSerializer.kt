package xyz.rose.gateway.core.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * Serializes a set of keyed objects that correspond to GatewayConfigSchema instances.
 *
 * @constructor Create a new Combined gateway schema serializer
 */
class CombinedGatewaySchemaSerializer(
    private val schemas: Map<String, ConfigSchema<Any>>
) : KSerializer<Map<String, Any>> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CombinedGatewayConfig") {
        schemas.forEach { (key, schema) ->
            element(key, schema.serializer.descriptor)
        }
    }

    override fun serialize(encoder: Encoder, value: Map<String, Any>) {
        encoder.encodeStructure(descriptor) {
            var index = 0

            schemas.forEach { (key, schema) ->
                val data = value[key] ?: throw SerializationException("Missing config key: $key")

                // Use the specific schema's serializer to encode the value
                @Suppress("UNCHECKED_CAST") encodeSerializableElement(descriptor, index++, schema.serializer, data)
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Map<String, Any> {
        val result = mutableMapOf<String, Any>()

        decoder.decodeStructure(descriptor) {
            while (true) {
                val index = decodeElementIndex(descriptor)

                if (index == CompositeDecoder.DECODE_DONE) break

                // Get the key/schema based on the index from the descriptor
                val key = descriptor.getElementName(index)
                val schema = schemas[key] ?: throw SerializationException("Unknown config key: $key")

                // Use the specific schema's serializer to decode the value
                val value = decodeSerializableElement(descriptor, index, schema.serializer)
                result[key] = value
            }
        }

        return result
    }
}
