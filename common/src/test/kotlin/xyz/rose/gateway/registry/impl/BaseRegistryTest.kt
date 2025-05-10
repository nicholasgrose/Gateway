package xyz.rose.gateway.registry.impl

import xyz.rose.gateway.registry.Registrar
import xyz.rose.gateway.registry.RegistryItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BaseRegistryTest {
    // Test implementation of RegistryItem
    private class TestItem(val name: String) : RegistryItem

    // Test implementation of Registrar
    private class TestRegistrar(private val items: List<TestItem>) : Registrar<TestItem> {
        override fun registrations(): List<TestItem> = items
    }

    // Concrete implementation of BaseRegistry for testing
    private class TestRegistry : BaseRegistry<TestItem>() {
        override fun generateId(item: TestItem): String = item.name
    }

    @Test
    fun `test register item`() {
        // Arrange
        val registry = TestRegistry()
        val item = TestItem("test-item")

        // Act
        registry.register(item)

        // Assert
        assertEquals(1, registry.getAll().size)
        assertEquals(item, registry.getById("test-item"))
    }

    @Test
    fun `test register items from registrar`() {
        // Arrange
        val registry = TestRegistry()
        val items = listOf(TestItem("item1"), TestItem("item2"), TestItem("item3"))
        val registrar = TestRegistrar(items)

        // Act
        registry.register(registrar)

        // Assert
        assertEquals(3, registry.getAll().size)
        assertEquals(items[0], registry.getById("item1"))
        assertEquals(items[1], registry.getById("item2"))
        assertEquals(items[2], registry.getById("item3"))
    }

    @Test
    fun `test deregister item`() {
        // Arrange
        val registry = TestRegistry()
        val item = TestItem("test-item")
        registry.register(item)

        // Act
        registry.deregister(item)

        // Assert
        assertTrue(registry.getAll().isEmpty())
        assertNull(registry.getById("test-item"))
    }

    @Test
    fun `test getAll returns all registered items`() {
        // Arrange
        val registry = TestRegistry()
        val items = listOf(TestItem("item1"), TestItem("item2"), TestItem("item3"))
        items.forEach { registry.register(it) }

        // Act
        val result = registry.getAll()

        // Assert
        assertEquals(3, result.size)
        assertTrue(result.containsAll(items))
    }

    @Test
    fun `test getById returns correct item`() {
        // Arrange
        val registry = TestRegistry()
        val item = TestItem("test-item")
        registry.register(item)

        // Act
        val result = registry.getById("test-item")

        // Assert
        assertEquals(item, result)
    }

    @Test
    fun `test getById returns null for non-existent id`() {
        // Arrange
        val registry = TestRegistry()

        // Act
        val result = registry.getById("non-existent")

        // Assert
        assertNull(result)
    }
}
