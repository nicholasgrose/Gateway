package xyz.rose.gateway.registry.impl

import xyz.rose.gateway.plugin.Plugin
import xyz.rose.gateway.registry.PluginRegistry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PluginRegistryImplTest {
    // Mock implementation of Plugin
    private class TestPlugin : Plugin

    // Another mock implementation of Plugin for testing multiple plugins
    private class AnotherTestPlugin : Plugin

    @Test
    fun `test registerWithData registers plugin and returns data`() {
        // Arrange
        val registry = PluginRegistryImpl()
        val plugin = TestPlugin()

        // Act
        val data = registry.registerWithData(plugin)

        // Assert
        assertEquals("TestPlugin", data.pluginId)
        assertEquals(plugin, registry.getById("TestPlugin"))
    }

    @Test
    fun `test register adds plugin to registry`() {
        // Arrange
        val registry = PluginRegistryImpl()
        val plugin = TestPlugin()

        // Act
        registry.register(plugin)

        // Assert
        assertEquals(1, registry.getAll().size)
        assertEquals(plugin, registry.getById("TestPlugin"))
    }

    @Test
    fun `test deregister removes plugin from registry`() {
        // Arrange
        val registry = PluginRegistryImpl()
        val plugin = TestPlugin()
        registry.register(plugin)

        // Act
        registry.deregister(plugin)

        // Assert
        assertTrue(registry.getAll().isEmpty())
    }

    @Test
    fun `test registry can handle multiple plugins`() {
        // Arrange
        val registry = PluginRegistryImpl()
        val plugin1 = TestPlugin()
        val plugin2 = AnotherTestPlugin()

        // Act
        registry.register(plugin1)
        registry.register(plugin2)

        // Assert
        assertEquals(2, registry.getAll().size)
        assertEquals(plugin1, registry.getById("TestPlugin"))
        assertEquals(plugin2, registry.getById("AnotherTestPlugin"))
    }
}
