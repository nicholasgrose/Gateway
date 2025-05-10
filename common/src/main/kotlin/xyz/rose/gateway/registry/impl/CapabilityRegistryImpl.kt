package xyz.rose.gateway.registry.impl

import xyz.rose.gateway.capability.Capability
import xyz.rose.gateway.registry.CapabilityRegistry

/**
 * Implementation of the CapabilityRegistry interface.
 */
class CapabilityRegistryImpl : BaseRegistry<Capability>(), CapabilityRegistry {
    /**
     * Generates a unique ID for a capability.
     *
     * @param capability The capability to generate an ID for.
     * @return A unique ID for the capability.
     */
    override fun generateId(capability: Capability): String {
        return capability.javaClass.simpleName
    }

    /**
     * Registers a capability and returns registration data.
     *
     * @param capability The capability to register.
     * @return Data about the registration.
     */
    override fun registerWithData(capability: Capability): CapabilityRegistry.CapabilityRegistrationData {
        val id = generateId(capability)
        items[id] = capability
        return CapabilityRegistry.CapabilityRegistrationData(id)
    }
}
