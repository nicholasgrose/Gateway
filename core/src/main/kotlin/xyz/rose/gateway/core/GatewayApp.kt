package xyz.rose.gateway.core

/**
 * The core Gateway app.
 * This is the primary entry point for starting and stopping Gateway integrations.
 *
 * @constructor Create a new Gateway app
 */
interface GatewayApp {
    /**
     * Start the Gateway app.
     *
     * This will do the following in order:
     * 1. Connect all platforms
     * 2. Enable all registered capabilities
     * 3. Enable all registered plugins
     */
    suspend fun start()

    /**
     * Stop the Gateway app.
     *
     * This will do the following in order:
     * 1. Disable all registered plugins
     * 2. Disable all registered capabilities
     * 3. Disconnect all platforms
     */
    suspend fun stop()
}
