package xyz.rose.gateway.registry.impl

import xyz.rose.gateway.platform.Platform
import xyz.rose.gateway.registry.PlatformRegistry

/**
 * Implementation of the PlatformRegistry interface.
 */
class PlatformRegistryImpl : BaseRegistry<Platform>(), PlatformRegistry {
    /**
     * Generates a unique ID for a platform.
     *
     * @param platform The platform to generate an ID for.
     * @return A unique ID for the platform.
     */
    override fun generateId(platform: Platform): String {
        return platform.javaClass.simpleName
    }

    /**
     * Registers a platform and returns registration data.
     *
     * @param platform The platform to register.
     * @return Data about the registration.
     */
    override fun registerWithData(platform: Platform): PlatformRegistry.PlatformRegistrationData {
        val id = generateId(platform)
        items[id] = platform
        return PlatformRegistry.PlatformRegistrationData(id)
    }
}
