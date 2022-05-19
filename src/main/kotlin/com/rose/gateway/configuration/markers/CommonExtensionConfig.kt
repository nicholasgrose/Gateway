package com.rose.gateway.configuration.markers

import com.rose.gateway.bot.DiscordBot
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class CommonExtensionConfig<T>(private val extensionName: String) : KoinComponent, ConfigObject {
    val bot: DiscordBot by inject()

    fun modifyExtensionLoadedStatus(enabled: Boolean) {
        runBlocking {
            if (enabled) {
                bot.bot?.loadExtension(extensionName)
            } else {
                bot.bot?.unloadExtension(extensionName)
            }
        }
    }
}
