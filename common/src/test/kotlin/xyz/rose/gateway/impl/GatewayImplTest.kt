package xyz.rose.gateway.impl

import xyz.rose.gateway.config.GatewayPlatformConfig
import xyz.rose.gateway.platform.Platform
import xyz.rose.gateway.registry.impl.PlatformRegistryImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GatewayImplTest {
    // Mock implementation of GatewayPlatformConfig
    private class TestPlatformConfig : GatewayPlatformConfig

    // Mock implementation of Platform
    private class TestPlatform : Platform {
        override fun loadConfig(): GatewayPlatformConfig = TestPlatformConfig()
    }

    @Test
    fun `test create returns new GatewayImpl instance`() {
        // Act
        val gateway = GatewayImpl.create()

        // Assert
        assertTrue(gateway is GatewayImpl)
    }

    @Test
    fun `test connect registers platform in platform registry`() {
        // Arrange
        val gateway = GatewayImpl()
        val platform = TestPlatform()

        // Act
        gateway.connect(platform)

        // Assert
        val platformRegistryImpl = gateway.platformRegistry as PlatformRegistryImpl
        val registeredPlatform = platformRegistryImpl.getById("TestPlatform")
        assertNotNull(registeredPlatform)
        assertEquals(platform, registeredPlatform)
    }

    @Test
    fun `test disconnect deregisters platform from platform registry`() {
        // Arrange
        val gateway = GatewayImpl()
        val platform = TestPlatform()
        gateway.connect(platform)

        // Act
        gateway.disconnect(platform)

        // Assert
        val platformRegistryImpl = gateway.platformRegistry as PlatformRegistryImpl
        val registeredPlatform = platformRegistryImpl.getById("TestPlatform")
        assertEquals(null, registeredPlatform)
    }

    @Test
    fun `test gateway has all required registries`() {
        // Arrange
        val gateway = GatewayImpl()

        // Assert
        assertNotNull(gateway.platformRegistry)
        assertNotNull(gateway.pluginRegistry)
        assertNotNull(gateway.capabilityRegistry)
    }
}
