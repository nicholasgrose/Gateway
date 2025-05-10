package xyz.rose.gateway.minecraft.capability

import xyz.rose.gateway.capability.AllowlistReadCapability

/**
 * Interface for capabilities that provide read access to the Minecraft whitelist.
 * This capability allows reading the list of whitelisted players.
 */
interface MinecraftWhitelistReadCapability : AllowlistReadCapability {
    /**
     * Gets the list of whitelisted players.
     *
     * @return A list of whitelisted player names.
     */
    override fun getAllowedEntities(): List<String>

    /**
     * Checks if a player is whitelisted.
     *
     * @param entityId The name of the player to check.
     * @return True if the player is whitelisted, false otherwise.
     */
    override fun isEntityAllowed(entityId: String): Boolean
}
