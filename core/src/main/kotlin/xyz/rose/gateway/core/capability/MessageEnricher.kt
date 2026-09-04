package xyz.rose.gateway.core.capability

import kotlin.reflect.KClass

/**
 * A capability that can enrich messages with platform-specific data
 */
interface MessageEnricher<T : GatewayMessageData> : Capability {
    /**
     * The type of message this enricher can enrich.
     */
    val dataType: KClass<T>

    /**
     * Enriches a message with platform-specific data.
     *
     * @param message The message to enrich
     * @return The metadata to add to the message or null should be added
     */
    suspend fun enrich(message: GatewayMessage<T>): MessageMetadata?
}
