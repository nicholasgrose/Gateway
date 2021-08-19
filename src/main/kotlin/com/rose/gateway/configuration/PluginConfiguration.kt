package com.rose.gateway.configuration

import com.rose.gateway.configuration.specs.PluginSpec
import com.uchuhimo.konf.Config
import com.uchuhimo.konf.Item

class PluginConfiguration {
    companion object {
        private const val CONFIGURATION_FILE = "plugins/Gateway/Gateway.yaml"
        private const val EXAMPLE_CONFIGURATION_URL =
            "https://raw.githubusercontent.com/nicholasgrose/Gateway/main/examples/Gateway.yaml"
    }

    val configurationStringMap = ConfigurationStringMap(PluginSpec)
    private val configurationLoader = ConfigurationLoader(PluginSpec, CONFIGURATION_FILE, EXAMPLE_CONFIGURATION_URL)
    var configuration: Config? = configurationLoader.loadOrCreateConfig()

    fun reloadConfiguration() {
        configuration = configurationLoader.loadOrCreateConfig()
    }

    fun configurationNotLoaded(): Boolean {
        return configuration == null
    }

    operator fun <T> get(item: Item<T>): T {
        return configuration!![item]
    }

    operator fun <T> set(item: Item<T>, newValue: T) {
        configuration!![item] = newValue
    }
}
