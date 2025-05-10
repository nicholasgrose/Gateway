package xyz.rose.gateway.plugin

import xyz.rose.gateway.registry.RegistryItem

/**
 * Interface for plugins that provide functionality for a platform.
 * Plugins group related pieces of functionality together for ease of organization or reasoning.
 *
 * Examples of plugins:
 * - Minecraft whitelist functionality
 * - Handling of messages from a particular platform
 */
interface Plugin : RegistryItem
