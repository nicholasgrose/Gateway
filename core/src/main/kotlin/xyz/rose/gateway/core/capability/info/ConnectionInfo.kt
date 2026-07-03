package xyz.rose.gateway.core.capability.info

import xyz.rose.gateway.core.capability.Capability

/**
 * Platform connection information
 */
interface ConnectionInfo : Capability {
    /**
     * Hostname or IP address
     */
    val host: String

    /**
     * Port number
     */
    val port: Int
}
