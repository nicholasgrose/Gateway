package xyz.rose.gateway.core.capability

/**
 * Platform connection information
 */
interface ConnectionInfo : GatewayCapability {
    /**
     * Hostname or IP address
     */
    val host: String

    /**
     * Port number
     */
    val port: Int
}
