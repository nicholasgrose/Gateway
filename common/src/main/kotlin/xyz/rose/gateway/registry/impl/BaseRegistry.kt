package xyz.rose.gateway.registry.impl

import xyz.rose.gateway.registry.Registrar
import xyz.rose.gateway.registry.Registry
import xyz.rose.gateway.registry.RegistryItem

/**
 * Base implementation of the Registry interface.
 *
 * @param I The type of items that this registry can register.
 */
abstract class BaseRegistry<I : RegistryItem> : Registry<I> {
    protected val items = mutableMapOf<String, I>()

    /**
     * Generates a unique ID for an item.
     *
     * @param item The item to generate an ID for.
     * @return A unique ID for the item.
     */
    protected abstract fun generateId(item: I): String

    /**
     * Registers an item in the registry.
     *
     * @param item The item to register.
     */
    override fun register(item: I) {
        val id = generateId(item)
        items[id] = item
    }

    /**
     * Registers all items provided by a registrar.
     *
     * @param registrar The registrar providing the items to register.
     */
    override fun register(registrar: Registrar<I>) {
        registrar.registrations().forEach { register(it) }
    }

    /**
     * Deregisters an item from the registry.
     *
     * @param item The item to deregister.
     */
    override fun deregister(item: I) {
        val id = generateId(item)
        items.remove(id)
    }

    /**
     * Gets all registered items.
     *
     * @return A collection of all registered items.
     */
    fun getAll(): Collection<I> {
        return items.values
    }

    /**
     * Gets an item by its ID.
     *
     * @param id The ID of the item to get.
     * @return The item with the given ID, or null if no such item exists.
     */
    fun getById(id: String): I? {
        return items[id]
    }
}
