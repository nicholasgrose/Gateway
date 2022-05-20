package com.rose.gateway.configuration

import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.schema.Config
import com.rose.gateway.shared.configurations.asClass
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PluginConfiguration : KoinComponent {
    private val bot: DiscordBot by inject()
    val stringMap: ConfigurationStringMap by inject()

    private val configurationLoader = GatewayConfigLoader()
    var config: Config = runBlocking {
        configurationLoader.loadOrCreateConfig()
    }

    suspend fun reloadConfiguration(): Boolean {
        config = configurationLoader.loadOrCreateConfig()

        return if (notLoaded()) {
            bot.stop()

            false
        } else {
            true
        }
    }

    fun saveConfiguration() {
        configurationLoader.saveConfig(config)
    }

    fun notLoaded(): Boolean = config == DEFAULT_CONFIG

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
