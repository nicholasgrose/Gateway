package xyz.rose.gateway.core.capability.message

import xyz.rose.gateway.core.capability.Capability

/**
 * A capability that can enrich messages with platform-specific data
 */
interface MessageEnricher : Capability {
    /**
     * Enriches a message with platform-specific data
     *
     * @param message The message to enrich
     * @return The enriched message
     */
    fun enrich(message: Message): Message
}
