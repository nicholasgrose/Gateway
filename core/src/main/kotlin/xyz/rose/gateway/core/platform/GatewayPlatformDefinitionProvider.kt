package xyz.rose.gateway.core.platform

/**
 * A utility interface for providing platform-specific definitions.
 * This just gives a quick way to implement similarly named/documented functions on different platforms' companion objects.
 *
 * @constructor Create a new Gateway platform definition provider
 */
interface GatewayPlatformDefinitionProvider {
    /**
     * The definition for this platform.
     *
     * @return The platform definition
     */
    fun definition(): GatewayPlatformDefinition<*>
}
