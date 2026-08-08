package xyz.rose.gateway.core.capability.message

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.EnricherMetadata
import xyz.rose.gateway.core.util.UUIDSerializer
import xyz.rose.gateway.core.util.InstantSerializer
import java.time.Instant
import java.util.*

/**
 * A message that represents a request for the player list.
 * Platforms enrich this message with their player lists.
 */
@Serializable
data class PlayerListMessage(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID = UUID.randomUUID(),
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant = Instant.now(),
    override val sourcePlatformId: String,
    override val metadata: MutableMap<String, @Polymorphic EnricherMetadata> = mutableMapOf()
) : GatewayMessage
