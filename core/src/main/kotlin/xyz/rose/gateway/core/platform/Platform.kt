package xyz.rose.gateway.core.platform

import xyz.rose.gateway.core.capability.Capability

/**
 * A platform connection controller for Gateway.
 */
interface Platform {
    /**
     * A unique identifier for this platform instance.
     */
    val id: String

    /**
     * Information about the platform.
     */
    val info: PlatformInfo

    /**
     * The capabilities provided by this platform.
     */
    val capabilities: List<Capability>

    /**
     * Connect to the platform.
     *
     * If connecting fails, the platform should gracefully handle failures.
     * However, if the Gateway app cannot recover from the failure, it should throw an exception.
     */
    suspend fun connect()

    /**
     * Disconnect from the platform.
     *
     * Disconnection should be graceful and not throw exceptions to ensure that other shutdown procedures can continue.
     */
    suspend fun disconnect()
}
