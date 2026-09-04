package xyz.rose.gateway.core.capability.info.version

import xyz.rose.gateway.core.PlatformGatewayClient
import xyz.rose.gateway.core.capability.MessageMetadataMap

/**
 * A platform helper for querying version information.
 */
class VersionQuery(private val client: PlatformGatewayClient) {
    /**
     * Queries the version information of platforms.
     *
     * @param metadata The metadata to send with the request.
     * @return The response.
     */
    suspend fun query(metadata: MessageMetadataMap = emptyMap()): Response {
        val enriched = client.query(VersionRequestData(), metadata)

        return Response(
            version = "2.0.0",
            platformVersions = enriched.metadata
        )
    }

    /**
     * A response to a version query.
     *
     * @property version The version of Gateway core.
     * @property platformVersions Version metadata provided by platforms.
     */
    data class Response(
        val version: String,
        val platformVersions: MessageMetadataMap
    )
}
