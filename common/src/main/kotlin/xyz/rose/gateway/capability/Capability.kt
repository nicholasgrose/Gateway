package xyz.rose.gateway.capability

import xyz.rose.gateway.registry.RegistryItem

/**
 * Interface for capabilities that provide specific functionality for a platform.
 * If platforms are "what are we connecting to", capabilities are "what can we do with this connection".
 * Capabilities are how we access platform-specific as well as general functionality.
 *
 * Examples of capabilities:
 * - Getting the list of online players in Minecraft
 * - Sending a message
 * - Parsing a message from a particular platform
 */
interface Capability : RegistryItem
