package xyz.rose.gateway.core.capability

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import xyz.rose.gateway.core.platform.PlatformType
import xyz.rose.gateway.core.platform.PlatformUID
import xyz.rose.gateway.core.util.InstantSerializer
import xyz.rose.gateway.core.util.UuidSerializer
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * A message that can be dispatched through Gateway.
 */
@Serializable
data class GatewayMessage<T : GatewayMessageData>(
    /**
     * A unique identifier for this message.
     */
    @Serializable(with = UuidSerializer::class)
    val id: Uuid = Uuid.random(),

    /**
     * The time when this message was created.
     */
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant = Clock.System.now(),

    /**
     * The type of platform that produced this message.
     */
    val sourcePlatformType: PlatformType,

    /**
     * The ID of the platform that produced this message.
     */
    val sourcePlatformId: PlatformUID,

    /**
     * The content of this message.
     */
    @Polymorphic
    val data: T,

    /**
     * A map of metadata for this message.
     */
    val metadata: MessageMetadataMap
)

/**
 * Map of metadata for a [GatewayMessage] object.
 */
typealias MessageMetadataMap = Map<PlatformType, Map<PlatformUID, List<@Polymorphic MessageMetadata>>>

/**
 * Marker interface for metadata that can be added to a [GatewayMessage] object.
 */
interface MessageMetadata

/**
 * Marker interface for data that can be added to a [GatewayMessage] object.
 */
interface GatewayMessageData
