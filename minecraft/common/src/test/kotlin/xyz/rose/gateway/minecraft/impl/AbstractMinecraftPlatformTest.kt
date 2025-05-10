package xyz.rose.gateway.minecraft.impl

import xyz.rose.gateway.config.GatewayPlatformConfig
import xyz.rose.gateway.minecraft.config.MinecraftColorConfig
import xyz.rose.gateway.minecraft.config.MinecraftConfig
import xyz.rose.gateway.minecraft.plugin.MinecraftChatPlugin
import xyz.rose.gateway.minecraft.plugin.MinecraftPlayerCountPlugin
import xyz.rose.gateway.minecraft.plugin.MinecraftTpsPlugin
import xyz.rose.gateway.minecraft.plugin.MinecraftVersionInfoPlugin
import xyz.rose.gateway.minecraft.plugin.MinecraftWhitelistPlugin
import xyz.rose.gateway.plugin.Plugin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AbstractMinecraftPlatformTest {
    // Mock implementation of MinecraftColorConfig
    private class TestMinecraftColorConfig : MinecraftColorConfig {
        override val primary: String = "test-primary"
        override val secondary: String = "test-secondary"
        override val tertiary: String = "test-tertiary"
        override val warning: String = "test-warning"
    }

    // Mock implementation of MinecraftConfig
    private class TestMinecraftConfig : MinecraftConfig {
        override val colors: MinecraftColorConfig = TestMinecraftColorConfig()
    }

    // Concrete implementation of AbstractMinecraftPlatform for testing
    private class TestMinecraftPlatform(config: MinecraftConfig) : AbstractMinecraftPlatform(config)

    @Test
    fun `test loadConfig returns the config`() {
        // Arrange
        val config = TestMinecraftConfig()
        val platform = TestMinecraftPlatform(config)

        // Act
        val result = platform.loadConfig()

        // Assert
        assertEquals(config, result)
    }

    @Test
    fun `test registrations returns all plugins`() {
        // Arrange
        val config = TestMinecraftConfig()
        val platform = TestMinecraftPlatform(config)

        // Act
        val plugins = platform.registrations()

        // Assert
        assertEquals(5, plugins.size)
        assertTrue(plugins.any { it is xyz.rose.gateway.minecraft.plugin.MinecraftWhitelistPlugin })
        assertTrue(plugins.any { it is xyz.rose.gateway.minecraft.plugin.MinecraftTpsPlugin })
        assertTrue(plugins.any { it is xyz.rose.gateway.minecraft.plugin.MinecraftVersionInfoPlugin })
        assertTrue(plugins.any { it is xyz.rose.gateway.minecraft.plugin.MinecraftPlayerCountPlugin })
        assertTrue(plugins.any { it is xyz.rose.gateway.minecraft.plugin.MinecraftChatPlugin })
    }

    @Test
    fun `test config property returns the config`() {
        // Arrange
        val config = TestMinecraftConfig()
        val platform = TestMinecraftPlatform(config)

        // Act & Assert
        assertEquals(config, platform.config)
    }
}
