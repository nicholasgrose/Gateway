package com.rose.gateway.configuration

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.configuration.specs.PluginSpec
import com.uchuhimo.konf.Config
import com.uchuhimo.konf.Item
import com.uchuhimo.konf.source.yaml.toYaml
import kotlinx.coroutines.runBlocking

class PluginConfiguration(private val plugin: GatewayPlugin) {
    companion object {
        const val CONFIG_FILE_NAME = "config.yaml"
    }

    private val pluginDirPath = plugin.dataFolder.path
    private val configFilePath = "$pluginDirPath/$CONFIG_FILE_NAME"
    private val exampleConfigurationUrl =
        "https://raw.githubusercontent.com/nicholasgrose/Gateway/v${plugin.version}/examples/$CONFIG_FILE_NAME"

    val configurationStringMap = ConfigurationStringMap(PluginSpec)
    private val configurationLoader = ConfigurationLoader(PluginSpec, configFilePath, exampleConfigurationUrl)
    var configuration: Config? = configurationLoader.loadOrCreateConfig()

    fun reloadConfiguration(): Boolean {
        configuration = configurationLoader.loadOrCreateConfig()

        return if (configuration == null) {
            runBlocking {
                plugin.discordBot.stop()
            }

            false
        } else {
            true
        }
    }

    fun notLoaded(): Boolean {
        return configuration == null
    }

    operator fun <T> get(item: Item<T>): T? {
        val config = configuration ?: return null

        return config[item]
    }

    operator fun <T> set(item: Item<T>, newValue: T) {
        val config = configuration ?: return

        config[item] = newValue
        config.toYaml.toFile(configFilePath)
    }
}
