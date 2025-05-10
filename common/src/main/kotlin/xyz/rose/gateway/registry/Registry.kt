package xyz.rose.gateway.registry

/**
 * Base interface for all registries.
 *
 * @param I The type of items that this registry can register.
 */
interface Registry<I : RegistryItem> {
    /**
     * Registers an item in the registry.
     *
     * @param item The item to register.
     */
    fun register(item: I)

    /**
     * Registers all items provided by a registrar.
     *
     * @param registrar The registrar providing the items to register.
     */
    fun register(registrar: Registrar<I>)

    /**
     * Deregisters an item from the registry.
     *
     * @param item The item to deregister.
     */
    fun deregister(item: I)
}
