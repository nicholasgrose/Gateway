package xyz.rose.gateway.minecraft.capability

import xyz.rose.gateway.capability.AllowlistWriteCapability

/**
 * Interface for capabilities that provide write access to the Minecraft whitelist.
 * This capability allows modifying the list of whitelisted players.
 */
interface MinecraftWhitelistWriteCapability : AllowlistWriteCapability {
    /**
     * Adds a player to the whitelist.
     *
     * @param entityId The name of the player to add.
     * @return True if the player was added, false if they were already whitelisted.
     */
    override fun addEntityToAllowlist(entityId: String): Boolean

    /**
     * Removes a player from the whitelist.
     *
     * @param entityId The name of the player to remove.
     * @return True if the player was removed, false if they weren't whitelisted.
     */
    override fun removeEntityFromAllowlist(entityId: String): Boolean
}
