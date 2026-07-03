package xyz.rose.gateway.core.platform

/**
 * A platform connection controller for Gateway.
 *
 * @constructor Create a new Gateway platform
 */
interface Platform {
    /**
     * Connect to the platform.
     *
     * If connecting fails, the platform should gracefully handle failures.
     * However, if the Gateway app cannot recover from the failure, it should throw an exception.
     */
    fun connect()

    /**
     * Disconnect from the platform.
     *
     * Disconnection should be graceful and not throw exceptions to ensure that other shutdown procedures can continue.
     */
    fun disconnect()
}
