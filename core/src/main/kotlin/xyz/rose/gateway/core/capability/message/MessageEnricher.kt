package xyz.rose.gateway.core.capability.message

import xyz.rose.gateway.core.capability.Capability

/**
 * A capability that can enrich messages with platform-specific data
 */
interface MessageEnricher : Capability {
    /**
     * Enriches a message with platform-specific data.
     *
     * Enrichers should add their data to the [GatewayMessage.metadata] map.
     *
     * @param message The message to enrich
     */
    suspend fun enrich(message: GatewayMessage)
}
