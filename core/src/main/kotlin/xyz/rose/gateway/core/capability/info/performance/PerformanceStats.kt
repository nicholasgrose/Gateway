package xyz.rose.gateway.core.capability.info.performance

import xyz.rose.gateway.core.PlatformGatewayClient
import xyz.rose.gateway.core.capability.MessageMetadataMap

/**
 * Gateway capability for performance metrics
 */
class PerformanceStats(private val client: PlatformGatewayClient) {
    /**
     * Queries the performance stats of platforms.
     *
     * @param metadata The metadata to send with the request.
     * @return The response.
     */
    suspend fun query(metadata: MessageMetadataMap = emptyMap()): Response {
        val enriched = client.query(PerformanceRequestData(), metadata)

        return Response(enriched.metadata)
    }

    /**
     * A response to a performance stats query.
     *
     * @property metadata Performance metadata provided by platforms.
     */
    data class Response(val metadata: MessageMetadataMap)
}
