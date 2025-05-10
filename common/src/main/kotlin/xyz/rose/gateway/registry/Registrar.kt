package xyz.rose.gateway.registry

/**
 * Interface for classes that can register multiple items of a specific type in a registry.
 *
 * @param I The type of items that this registrar can register.
 */
interface Registrar<I : RegistryItem> {
    /**
     * Returns a list of items to be registered.
     *
     * @return A list of items to be registered.
     */
    fun registrations(): List<I>
}
