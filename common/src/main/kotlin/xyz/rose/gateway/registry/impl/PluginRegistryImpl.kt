package xyz.rose.gateway.registry.impl

import xyz.rose.gateway.plugin.Plugin
import xyz.rose.gateway.registry.PluginRegistry

/**
 * Implementation of the PluginRegistry interface.
 */
class PluginRegistryImpl : BaseRegistry<Plugin>(), PluginRegistry {
    /**
     * Generates a unique ID for a plugin.
     *
     * @param plugin The plugin to generate an ID for.
     * @return A unique ID for the plugin.
     */
    override fun generateId(plugin: Plugin): String {
        return plugin.javaClass.simpleName
    }

    /**
     * Registers a plugin and returns registration data.
     *
     * @param plugin The plugin to register.
     * @return Data about the registration.
     */
    override fun registerWithData(plugin: Plugin): PluginRegistry.PluginRegistrationData {
        val id = generateId(plugin)
        items[id] = plugin
        return PluginRegistry.PluginRegistrationData(id)
    }
}
