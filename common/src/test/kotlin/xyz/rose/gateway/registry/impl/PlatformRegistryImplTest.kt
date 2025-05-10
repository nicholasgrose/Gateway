package xyz.rose.gateway.registry.impl

import xyz.rose.gateway.config.GatewayPlatformConfig
import xyz.rose.gateway.platform.Platform
import xyz.rose.gateway.registry.PlatformRegistry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PlatformRegistryImplTest {
    // Mock implementation of GatewayPlatformConfig
    private class TestPlatformConfig : GatewayPlatformConfig

    // Mock implementation of Platform
    private class TestPlatform : Platform {
        override fun loadConfig(): GatewayPlatformConfig = TestPlatformConfig()
    }

    // Another mock implementation of Platform for testing multiple platforms
    private class AnotherTestPlatform : Platform {
        override fun loadConfig(): GatewayPlatformConfig = TestPlatformConfig()
    }

    @Test
    fun `test registerWithData registers platform and returns data`() {
        // Arrange
        val registry = PlatformRegistryImpl()
        val platform = TestPlatform()

        // Act
        val data = registry.registerWithData(platform)

        // Assert
        assertEquals("TestPlatform", data.platformId)
        assertEquals(platform, registry.getById("TestPlatform"))
    }

    @Test
    fun `test register adds platform to registry`() {
        // Arrange
        val registry = PlatformRegistryImpl()
        val platform = TestPlatform()

        // Act
        registry.register(platform)

        // Assert
        assertEquals(1, registry.getAll().size)
        assertEquals(platform, registry.getById("TestPlatform"))
    }

    @Test
    fun `test deregister removes platform from registry`() {
        // Arrange
        val registry = PlatformRegistryImpl()
        val platform = TestPlatform()
        registry.register(platform)

        // Act
        registry.deregister(platform)

        // Assert
        assertTrue(registry.getAll().isEmpty())
    }

    @Test
    fun `test registry can handle multiple platforms`() {
        // Arrange
        val registry = PlatformRegistryImpl()
        val platform1 = TestPlatform()
        val platform2 = AnotherTestPlatform()

        // Act
        registry.register(platform1)
        registry.register(platform2)

        // Assert
        assertEquals(2, registry.getAll().size)
        assertEquals(platform1, registry.getById("TestPlatform"))
        assertEquals(platform2, registry.getById("AnotherTestPlatform"))
    }
}
