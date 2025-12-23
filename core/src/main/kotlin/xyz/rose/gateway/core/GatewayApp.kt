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
     * 1. Connect the platform
     * 2. Enable all registered plugins
     */
    fun start()

    /**
     * Stop the Gateway app.
     *
     * This will do the following in order:
     * 1. Disable all registered plugins
     * 2. Disconnect the platform
     */
    fun stop()
}
