package xyz.rose.gateway.core.capability.info.connection

import xyz.rose.gateway.core.PlatformGatewayClient
import xyz.rose.gateway.core.capability.MessageMetadataMap

/**
 * Platform connection information
 */
class ConnectionInfo(private val client: PlatformGatewayClient) {
    /**
     * Query the connection information from connected platforms.
     *
     * @param metadata The metadata to send with the request.
     * @return The response.
     */
    suspend fun query(metadata: MessageMetadataMap = emptyMap()): Response {
        val result = client.query(ConnectionInfoRequestData(), metadata)
        return Response(result.metadata)
    }

    /**
     * The connection responses from connected platforms.
     *
     * @property metadata The metadata from the response.
     */
    data class Response(
        val metadata: MessageMetadataMap
    )
}
