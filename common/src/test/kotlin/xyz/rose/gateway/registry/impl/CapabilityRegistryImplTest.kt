package xyz.rose.gateway.registry.impl

import xyz.rose.gateway.capability.Capability
import xyz.rose.gateway.registry.CapabilityRegistry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CapabilityRegistryImplTest {
    // Mock implementation of Capability
    private class TestCapability : Capability

    // Another mock implementation of Capability for testing multiple capabilities
    private class AnotherTestCapability : Capability

    @Test
    fun `test registerWithData registers capability and returns data`() {
        // Arrange
        val registry = CapabilityRegistryImpl()
        val capability = TestCapability()

        // Act
        val data = registry.registerWithData(capability)

        // Assert
        assertEquals("TestCapability", data.capabilityId)
        assertEquals(capability, registry.getById("TestCapability"))
    }

    @Test
    fun `test register adds capability to registry`() {
        // Arrange
        val registry = CapabilityRegistryImpl()
        val capability = TestCapability()

        // Act
        registry.register(capability)

        // Assert
        assertEquals(1, registry.getAll().size)
        assertEquals(capability, registry.getById("TestCapability"))
    }

    @Test
    fun `test deregister removes capability from registry`() {
        // Arrange
        val registry = CapabilityRegistryImpl()
        val capability = TestCapability()
        registry.register(capability)

        // Act
        registry.deregister(capability)

        // Assert
        assertTrue(registry.getAll().isEmpty())
    }

    @Test
    fun `test registry can handle multiple capabilities`() {
        // Arrange
        val registry = CapabilityRegistryImpl()
        val capability1 = TestCapability()
        val capability2 = AnotherTestCapability()

        // Act
        registry.register(capability1)
        registry.register(capability2)

        // Assert
        assertEquals(2, registry.getAll().size)
        assertEquals(capability1, registry.getById("TestCapability"))
        assertEquals(capability2, registry.getById("AnotherTestCapability"))
    }
}
