package com.rose.gateway.minecraft.chat

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.chatExtensionEnabled
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import com.rose.gateway.shared.concurrency.launchIf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * Launches a block of code if the chat extension is enabled
 *
 * @param config The config to look at for whether the extension is enabled
 * @param block The block of code to run if the extension is enabled
 * @receiver This [PluginCoroutineScope]
 * @return The launched job or null if none was launched, meaning the extension was not enabled
 */
fun PluginCoroutineScope.launchIfChatExtensionEnabled(
    config: PluginConfig,
    block: suspend CoroutineScope.() -> Unit
): Job? = launchIf(config.chatExtensionEnabled(), block)
