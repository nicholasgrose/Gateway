package xyz.rose.gateway.core.capability.info.online

import xyz.rose.gateway.core.PlatformGatewayClient
import xyz.rose.gateway.core.capability.MessageMetadataMap

/**
 * The stats of online users
 */
class OnlineStats(private val client: PlatformGatewayClient) {
    /**
     * Queries for online users from platforms.
     *
     * @param metadata The metadata of the request.
     * @return The response.
     */
    suspend fun query(metadata: MessageMetadataMap = emptyMap()): Response {
        val enriched = client.query(OnlineStatsRequestData(), metadata)

        return Response(enriched.metadata)
    }

    /**
     * The response to querying online users from platforms.
     *
     * @property metadata The metadata from the response.
     */
    data class Response(val metadata: MessageMetadataMap)
}
