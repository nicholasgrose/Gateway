package xyz.rose.gateway.discord.config

import xyz.rose.gateway.config.GatewayPlatformConfig

/**
 * Configuration for the Discord platform.
 *
 * @property token The Discord bot token.
 * @property bot Configuration for the Discord bot.
 */
interface DiscordConfig : GatewayPlatformConfig {
    val token: String
    val bot: BotConfig
}

/**
 * Configuration for the Discord bot.
 *
 * @property channels List of channel IDs that the bot should listen to.
 * @property extensions Configuration for bot extensions.
 */
interface BotConfig {
    val channels: List<String>
    val extensions: ExtensionConfig
}

/**
 * Configuration for Discord bot extensions.
 *
 * @property about Configuration for the about extension.
 * @property chat Configuration for the chat extension.
 * @property ip Configuration for the connection extension.
 * @property list Configuration for the player list extension.
 * @property performance Configuration for the performance extension.
 * @property allowlist Configuration for the allowlist extension.
 */
interface ExtensionConfig {
    val about: AboutConfig
    val chat: ChatConfig
    val ip: ConnectionConfig
    val list: PlayerListConfig
    val performance: PerformanceConfig
    val allowlist: AllowlistConfig
}

/**
 * Base configuration for all extensions.
 *
 * @property enabled Whether the extension is enabled.
 */
interface BaseExtensionConfig {
    val enabled: Boolean
}

/**
 * Configuration for the about extension.
 */
interface AboutConfig : BaseExtensionConfig

/**
 * Configuration for the chat extension.
 *
 * @property showRoleColor Whether to show role colors in chat messages.
 */
interface ChatConfig : BaseExtensionConfig {
    val showRoleColor: Boolean
}

/**
 * Configuration for the connection extension.
 *
 * @property displayIp The IP address to display for server connections.
 */
interface ConnectionConfig : BaseExtensionConfig {
    val displayIp: String
}

/**
 * Configuration for the player list extension.
 *
 * @property playersPerPage Number of players to display per page.
 */
interface PlayerListConfig : BaseExtensionConfig {
    val playersPerPage: Int
}

/**
 * Configuration for the performance extension.
 */
interface PerformanceConfig : BaseExtensionConfig

/**
 * Configuration for the allowlist extension.
 *
 * @property playersPerPage Number of players to display per page.
 */
interface AllowlistConfig : BaseExtensionConfig {
    val playersPerPage: Int
}
