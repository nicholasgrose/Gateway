package xyz.rose.gateway.capability

/**
 * Interface for capabilities that provide read access to version information.
 * This capability allows reading version information from a platform.
 */
interface VersionInfoReadCapability : Capability {
    /**
     * Gets the version of the platform.
     *
     * @return The version of the platform as a string.
     */
    fun getVersion(): String

    /**
     * Gets the name of the platform implementation.
     *
     * @return The name of the platform implementation as a string.
     */
    fun getImplementationName(): String

    /**
     * Gets the version of the platform implementation.
     *
     * @return The version of the platform implementation as a string.
     */
    fun getImplementationVersion(): String

    /**
     * Gets additional information about the platform.
     *
     * @return A map of additional information about the platform.
     */
    fun getAdditionalInfo(): Map<String, String>
}
