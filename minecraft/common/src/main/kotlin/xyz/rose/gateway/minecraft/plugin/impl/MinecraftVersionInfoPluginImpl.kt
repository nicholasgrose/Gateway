package xyz.rose.gateway.minecraft.plugin.impl

import xyz.rose.gateway.capability.Capability
import xyz.rose.gateway.minecraft.capability.MinecraftVersionInfoReadCapability
import xyz.rose.gateway.minecraft.plugin.MinecraftVersionInfoPlugin

/**
 * Implementation of the MinecraftVersionInfoPlugin interface.
 * This plugin provides functionality for retrieving information about the Minecraft server version.
 */
class MinecraftVersionInfoPluginImpl : MinecraftVersionInfoPlugin {
    /**
     * The capability for reading version information.
     */
    override val versionInfoReadCapability: MinecraftVersionInfoReadCapability = MinecraftVersionInfoReadCapabilityImpl()

    /**
     * Returns a list of capabilities to be registered.
     *
     * @return A list of capabilities to be registered.
     */
    override fun registrations(): List<Capability> {
        return listOf(versionInfoReadCapability)
    }

    /**
     * Implementation of the MinecraftVersionInfoReadCapability interface.
     */
    private inner class MinecraftVersionInfoReadCapabilityImpl : MinecraftVersionInfoReadCapability {
        override fun getVersion(): String {
            return getServerVersion()
        }

        override fun getImplementationName(): String {
            return getServerImplementation()
        }

        override fun getImplementationVersion(): String {
            return getServerImplementationVersion()
        }

        override fun getAdditionalInfo(): Map<String, String> {
            return mapOf(
                "javaVersion" to getJavaVersion(),
                "operatingSystem" to getOperatingSystem()
            )
        }
    }
    /**
     * Gets the Minecraft server version.
     *
     * @return The Minecraft server version.
     */
    fun getServerVersion(): String {
        // In a real implementation, this would interact with the Minecraft server
        return "1.20.1"
    }

    /**
     * Gets the Minecraft server implementation name (e.g., "Paper", "Spigot", "Vanilla").
     *
     * @return The Minecraft server implementation name.
     */
    fun getServerImplementation(): String {
        // In a real implementation, this would interact with the Minecraft server
        return "Paper"
    }

    /**
     * Gets the Minecraft server implementation version.
     *
     * @return The Minecraft server implementation version.
     */
    fun getServerImplementationVersion(): String {
        // In a real implementation, this would interact with the Minecraft server
        return "git-Paper-388 (MC: 1.20.1)"
    }

    /**
     * Gets the Java version the server is running on.
     *
     * @return The Java version.
     */
    fun getJavaVersion(): String {
        // In a real implementation, this would interact with the Minecraft server
        return System.getProperty("java.version")
    }

    /**
     * Gets the operating system the server is running on.
     *
     * @return The operating system.
     */
    fun getOperatingSystem(): String {
        // In a real implementation, this would interact with the Minecraft server
        return System.getProperty("os.name")
    }
}
