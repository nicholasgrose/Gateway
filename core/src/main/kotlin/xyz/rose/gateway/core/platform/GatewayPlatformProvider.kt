package xyz.rose.gateway.core.platform

import org.koin.core.module.Module
import xyz.rose.gateway.core.config.GatewayConfig
import xyz.rose.gateway.core.config.GatewayConfigSchema

/**
 * Provides the necessary data to instantiate a Gateway platform.
 *
 * A provider provides a schema for the configuration of the platform,
 * and a module for the runtime environment of the platform.
 *
 * @constructor Create a new Gateway platform provider
 */
interface GatewayPlatformProvider {
    /**
     * Gets the schema for the configuration of the platform.
     *
     * The schema defines the structure and validation rules for the configuration
     * of the platform, ensuring that the configuration is valid and complete when
     * loaded and used by Gateway.
     *
     * @return The schema for the configuration
     */
    fun getConfigSchema(): GatewayConfigSchema<*>

    /**
     * Creates a module for the runtime environment of the platform.
     *
     * The module defines the dependencies and behavior of the platform,
     * allowing Gateway to interact with the platform and its services.
     * This means it will include a platform, plugins, and capabilities for
     * interactions within Gateway and any services needed internally to run these.
     *
     * It is important to ensure that all platforms, plugins, and capabilities are bound to their generic interfaces.
     * Failing to do so will prevent Gateway from properly interacting with the platform and its services.
     *
     * @param config The loaded configuration for Gateway
     * @return The module for the runtime environment
     */
    fun createRuntimeModule(config: GatewayConfig): Module
}
