package xyz.rose.gateway.capability

/**
 * Interface for capabilities that provide read access to online entity counts.
 * This capability allows reading information about online entities from a platform.
 */
interface OnlineCountCapability : Capability {
    /**
     * Gets the number of online entities.
     *
     * @return The number of online entities.
     */
    fun getOnlineCount(): Int

    /**
     * Gets the maximum number of entities allowed.
     *
     * @return The maximum number of entities allowed.
     */
    fun getMaxCount(): Int

    /**
     * Gets a list of online entity identifiers.
     *
     * @return A list of online entity identifiers.
     */
    fun getOnlineEntityIds(): List<String>

    /**
     * Checks if an entity is online.
     *
     * @param entityId The identifier of the entity to check.
     * @return True if the entity is online, false otherwise.
     */
    fun isEntityOnline(entityId: String): Boolean
}
