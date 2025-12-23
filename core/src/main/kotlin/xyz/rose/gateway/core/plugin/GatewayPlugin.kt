package xyz.rose.gateway.core.plugin

/**
 * A plugin for a Gateway platform.
 *
 * Plugins contain bundles of logic that can be executed after platform capabilities have been registered.
 * This allows them to be the primary means of extending Gateway's functionality like with cross-platform integrations.
 * Plugins should not be used for core functionality or to provide basic platform services, and they should NOT assume
 * that other plugins are loaded, enabled, or disabled before they are.
 *
 * @constructor Create a new Gateway plugin
 */
interface GatewayPlugin {
    /**
     * Called when the Gateway app is being started.
     *
     * This is notably called:
     * - AFTER the platform is connected.
     * - AFTER all capabilities have been enabled.
     */
    fun onEnable()

    /**
     * This is called when the Gateway app is shutting down.
     *
     * This is notably called:
     * - BEFORE the platform is disconnected.
     * - BEFORE all capabilities have been disabled.
     */
    fun onDisable()
}
