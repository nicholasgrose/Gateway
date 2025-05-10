package xyz.rose.gateway.capability

/**
 * Interface for capabilities that provide read access to an allowlist.
 * This capability allows reading the list of allowed entities.
 */
interface AllowlistReadCapability : Capability {
    /**
     * Gets the list of allowed entities.
     *
     * @return A list of allowed entity identifiers.
     */
    fun getAllowedEntities(): List<String>

    /**
     * Checks if an entity is allowed.
     *
     * @param entityId The identifier of the entity to check.
     * @return True if the entity is allowed, false otherwise.
     */
    fun isEntityAllowed(entityId: String): Boolean
}
