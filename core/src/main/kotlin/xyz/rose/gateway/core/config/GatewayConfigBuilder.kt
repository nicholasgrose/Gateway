package xyz.rose.gateway.core.config

/**
 * Builds and configures a gateway configuration
 *
 * @constructor Create a new Gateway config builder
 */
interface GatewayConfigBuilder {
    /**
     * The schemas to include in the configuration
     */
    val schemas: MutableCollection<GatewayConfigSchema<*>>

    /**
     * Builds a gateway configuration
     *
     * @return The built configuration
     */
    fun build(): GatewayConfig
}
