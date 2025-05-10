package xyz.rose.gateway.registry

import xyz.rose.gateway.plugin.Plugin

/**
 * Registry for plugins.
 */
interface PluginRegistry : Registry<Plugin> {
    /**
     * Data returned when a plugin is registered.
     */
    data class PluginRegistrationData(
        val pluginId: String
    )

    /**
     * Registers a plugin and returns registration data.
     *
     * @param plugin The plugin to register.
     * @return Data about the registration.
     */
    fun registerWithData(plugin: Plugin): PluginRegistrationData
}
