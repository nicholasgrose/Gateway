package com.rose.gateway.minecraft.commands.completers

import com.rose.gateway.config.ConfigStringMap
import com.rose.gateway.config.Item
import com.rose.gateway.config.PluginConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConfigCompleter : KoinComponent {
    private val config: PluginConfig by inject()
    private val configStringMap: ConfigStringMap by inject()

    fun allConfigStrings(): List<String> = configStringMap.allStrings()

    fun allConfigItems(): List<Item<*>> = config.allItems()
}
