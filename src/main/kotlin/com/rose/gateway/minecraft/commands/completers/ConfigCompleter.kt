package com.rose.gateway.minecraft.commands.completers

import com.rose.gateway.configuration.ConfigurationStringMap
import com.rose.gateway.configuration.Item
import com.rose.gateway.configuration.PluginConfiguration
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConfigCompleter : KoinComponent {
    private val config: PluginConfiguration by inject()
    private val configStringMap: ConfigurationStringMap by inject()

    fun allConfigStrings(): List<String> = configStringMap.allStrings()

    fun allConfigItems(): List<Item<*>> = config.allItems()
}
