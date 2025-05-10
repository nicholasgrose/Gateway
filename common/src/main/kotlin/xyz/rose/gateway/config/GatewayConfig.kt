package xyz.rose.gateway.config

/**
 * Configuration for the Gateway application.
 *
 * @property platforms List of platform configurations.
 */
interface GatewayConfig {
    val platforms: List<GatewayPlatformConfig>
}

/**
 * Base interface for platform-specific configurations.
 */
interface GatewayPlatformConfig
