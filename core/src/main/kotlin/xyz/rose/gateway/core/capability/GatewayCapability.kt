package xyz.rose.gateway.core.capability

/**
 * A capability of a platform that Gateway can interact with.
 *
 * A capability is a feature or service provided by a platform that Gateway or another platform can get and use.
 * Capabilities allow Gateway to extend its functionality by leveraging the platform's features without needing to know
 * the specifics of how they work.
 * They are distinct from plugins because they are more focused on providing specific features or services that Gateway
 * can directly interact with, rather than extending Gateway's functionality through broader logic or integration.
 * They should be lightweight and focused on a single feature or service to maintain clarity and modularity.
 * Capabilities should also NOT assume that other capabilities are loaded, enabled, or disabled before they are.
 *
 * @constructor Create a new Gateway capability. This is called when the capability is first used.
 */
interface GatewayCapability {
    /**
     * Called when the Gateway app is being started.
     *
     * This is notably called:
     * - AFTER the platform is connected.
     * - BEFORE any plugins are enabled.
     */
    fun onEnable()

    /**
     * Called when the Gateway app is being shut down.
     *
     * This is notably called:
     * - BEFORE the platform is disconnected.
     * - AFTER all plugins are disabled.
     */
    fun onDisable()
}
