package xyz.rose.gateway.core.capability.message

import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import xyz.rose.gateway.core.Enrichable
import java.time.Instant
import java.util.*

/**
 * A message that can be dispatched through Gateway.
 */
interface GatewayMessage : Enrichable {
    /**
     * A unique identifier for this message.
     */
    val id: UUID

    /**
     * The time when this message was created.
     */
    val timestamp: Instant

    /**
     * The ID of the platform that produced this message.
     */
    val sourcePlatformId: String
}

/**
 * A [SerializersModule] that includes all [GatewayMessage] subtypes.
 */
val gatewayMessageSerializersModule = SerializersModule {
    polymorphic(GatewayMessage::class) {
        subclass(ChatMessage::class)
        subclass(VersionMessage::class)
        subclass(PlayerListMessage::class)
    }
}
