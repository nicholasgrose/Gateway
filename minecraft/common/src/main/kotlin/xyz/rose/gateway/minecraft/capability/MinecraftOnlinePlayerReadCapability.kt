package xyz.rose.gateway.minecraft.capability

import xyz.rose.gateway.capability.OnlineCountCapability

/**
 * Interface for capabilities that provide read access to online player counts in Minecraft.
 * This capability allows reading information about online players from a Minecraft server.
 */
interface MinecraftOnlinePlayerReadCapability : OnlineCountCapability {
    /**
     * Gets the number of online players.
     *
     * @return The number of online players.
     */
    override fun getOnlineCount(): Int

    /**
     * Gets the maximum number of players allowed on the server.
     *
     * @return The maximum number of players allowed.
     */
    override fun getMaxCount(): Int

    /**
     * Gets a list of online player names.
     *
     * @return A list of online player names.
     */
    override fun getOnlineEntityIds(): List<String>

    /**
     * Checks if a player is online.
     *
     * @param entityId The name of the player to check.
     * @return True if the player is online, false otherwise.
     */
    override fun isEntityOnline(entityId: String): Boolean
}
