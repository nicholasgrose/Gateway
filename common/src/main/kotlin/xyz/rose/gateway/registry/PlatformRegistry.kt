package xyz.rose.gateway.registry

import xyz.rose.gateway.platform.Platform

/**
 * Registry for platforms.
 */
interface PlatformRegistry : Registry<Platform> {
    /**
     * Data returned when a platform is registered.
     */
    data class PlatformRegistrationData(
        val platformId: String
    )

    /**
     * Registers a platform and returns registration data.
     *
     * @param platform The platform to register.
     * @return Data about the registration.
     */
    fun registerWithData(platform: Platform): PlatformRegistrationData
}
