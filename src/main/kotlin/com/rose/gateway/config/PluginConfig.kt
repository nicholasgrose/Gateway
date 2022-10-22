package com.rose.gateway.config

import com.rose.gateway.config.schema.Config
import com.rose.gateway.discord.bot.DiscordBot
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KType

/**
 * The configuration for the Gateway plugin
 *
 * @constructor Creates plugin config
 */
class PluginConfig : KoinComponent {
    private val bot: DiscordBot by inject()
    private val stringMap: ConfigStringMap by inject()
    private val pluginCoroutineScope: PluginCoroutineScope by inject()

    private val configFile = GatewayConfigFile()
    var config: Config = runBlocking {
        configFile.safelyLoadConfig()
    }

    /**
     * Reload the full config from the disk
     *
     * @return Whether the config was successfully loaded
     */
    suspend fun reloadConfig(): Boolean {
        config = configFile.safelyLoadConfig()

        return if (notLoaded()) {
            pluginCoroutineScope.launch {
                bot.close()
            }

            false
        } else {
            pluginCoroutineScope.launch {
                bot.rebuild()
            }

            true
        }
    }

    /**
     * Determines whether the config was loaded by comparing it to the default config
     *
     * @return Whether the user-defined config was successfully loaded
     */
    fun notLoaded(): Boolean = config == configFile.defaultConfig

    /**
     * Saves the current config to disk, replacing the existing config file
     */
    suspend fun saveConfig() {
        configFile.saveConfig(config)
    }

    /**
     * Gives a list of all available config items
     *
     * @return The list of config items
     */
    fun allItems(): List<Item<*>> = stringMap.itemMap.values.toList()

    /**
     * Gets a particular config item based on its path string
     *
     * @param item The config path to get the item with
     * @return The item that matches the provided path or null if no match was found
     */
    operator fun get(item: String): Item<*>? {
        return stringMap.fromString(item)
    }

    /**
     * Gets a particularly typed item based on its path string
     *
     * @param ItemValueType The type of the item to get
     * @param type The [KType] representation of [ItemValueType]
     * @param item The config path of the item to get
     * @return The item that matches the path and type provided or null if no match was found
     *
     * @see KType
     */
    operator fun <ItemValueType : Any> get(type: KType, item: String): Item<ItemValueType>? {
        val match = get(item)

        return if (match != null && match.type == type) {
            @Suppress("UNCHECKED_CAST")
            match as Item<ItemValueType>
        } else null
    }
}
