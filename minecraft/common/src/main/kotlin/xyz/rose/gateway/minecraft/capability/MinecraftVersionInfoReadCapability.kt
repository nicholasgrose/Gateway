package xyz.rose.gateway.minecraft.capability

import xyz.rose.gateway.capability.VersionInfoReadCapability

/**
 * Interface for capabilities that provide read access to Minecraft version information.
 * This capability allows reading version information from a Minecraft server.
 */
interface MinecraftVersionInfoReadCapability : VersionInfoReadCapability {
    /**
     * Gets the Minecraft server version.
     *
     * @return The Minecraft server version.
     */
    override fun getVersion(): String

    /**
     * Gets the Minecraft server implementation name (e.g., "Paper", "Spigot", "Vanilla").
     *
     * @return The Minecraft server implementation name.
     */
    override fun getImplementationName(): String

    /**
     * Gets the Minecraft server implementation version.
     *
     * @return The Minecraft server implementation version.
     */
    override fun getImplementationVersion(): String

    /**
     * Gets additional information about the Minecraft server.
     * This includes information such as Java version, operating system, etc.
     *
     * @return A map of additional information about the Minecraft server.
     */
    override fun getAdditionalInfo(): Map<String, String>
}
