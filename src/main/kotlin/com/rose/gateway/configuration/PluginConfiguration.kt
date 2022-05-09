package com.rose.gateway.configuration

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.configuration.schema.Config
import com.rose.gateway.shared.configurations.asClass
import kotlinx.coroutines.runBlocking

class PluginConfiguration(private val plugin: GatewayPlugin) {
    companion object {
        const val CONFIG_FILE_NAME = "config.yaml"
        const val REPOSITORY_RAW_URL = "https://raw.githubusercontent.com/nicholasgrose/Gateway"
    }

    private val pluginDirPath = plugin.dataFolder.path.replace("\\", "/")
    private val configFilePath = "$pluginDirPath/$CONFIG_FILE_NAME"
    private val exampleConfigurationUrl =
        "$REPOSITORY_RAW_URL/v${plugin.description.version}/examples/$CONFIG_FILE_NAME"

    val stringMap = ConfigurationStringMap()
    private val configurationLoader = GatewayConfigLoader(plugin, configFilePath, exampleConfigurationUrl)
    var config: Config? = runBlocking {
        configurationLoader.loadOrCreateConfig()
    }

    suspend fun reloadConfiguration(): Boolean {
        config = configurationLoader.loadOrCreateConfig()

        return if (config == null) {
            plugin.discordBot.stop()

            false
        } else {
            true
        }
    }

    fun notLoaded(): Boolean {
        return config == null
    }

    operator fun get(item: String): Item<*>? {
        return stringMap.fromString(item)
    }

    inline operator fun <reified T> get(item: String): T? {
        val matchingItem = stringMap.fromString(item) ?: return null

        return if (matchingItem.type().asClass() == T::class) {
            matchingItem.get() as T
        } else {
            null
        }
    }

    inline operator fun <reified T : Any> set(item: String, newValue: T) {
        val matchingItem = stringMap.fromString(item) ?: return

        if (matchingItem.type().asClass() == T::class) {
            @Suppress("UNCHECKED_CAST")
            (matchingItem as Item<T>).set(newValue)
        }
    }
}
