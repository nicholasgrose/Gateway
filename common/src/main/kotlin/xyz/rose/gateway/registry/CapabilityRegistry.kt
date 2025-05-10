package xyz.rose.gateway.registry

import xyz.rose.gateway.capability.Capability

/**
 * Registry for capabilities.
 */
interface CapabilityRegistry : Registry<Capability> {
    /**
     * Data returned when a capability is registered.
     */
    data class CapabilityRegistrationData(
        val capabilityId: String
    )

    /**
     * Registers a capability and returns registration data.
     *
     * @param capability The capability to register.
     * @return Data about the registration.
     */
    fun registerWithData(capability: Capability): CapabilityRegistrationData
}
