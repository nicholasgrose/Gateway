package xyz.rose.gateway.core.config

/**
 * The loaded config for Gateway
 *
 * @constructor Create a new Gateway config
 */
interface GatewayConfig {
    /**
     * Get a sub-config by key
     *
     * @param T The type of the config object
     * @param key The key of the config object
     * @return The config object
     */
    fun <T : Any> getConfig(key: String): T
}
