package xyz.rose.gateway.capability

/**
 * Interface for capabilities that provide write access to an allowlist.
 * This capability allows modifying the list of allowed entities.
 */
interface AllowlistWriteCapability : Capability {
    /**
     * Adds an entity to the allowlist.
     *
     * @param entityId The identifier of the entity to add.
     * @return True if the entity was added, false if it was already in the allowlist.
     */
    fun addEntityToAllowlist(entityId: String): Boolean

    /**
     * Removes an entity from the allowlist.
     *
     * @param entityId The identifier of the entity to remove.
     * @return True if the entity was removed, false if it wasn't in the allowlist.
     */
    fun removeEntityFromAllowlist(entityId: String): Boolean
}
