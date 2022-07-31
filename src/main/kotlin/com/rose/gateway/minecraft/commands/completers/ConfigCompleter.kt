package com.rose.gateway.minecraft.commands.completers

import com.rose.gateway.configuration.ConfigurationStringMap
import com.rose.gateway.minecraft.commands.arguments.ConfigItemArgs
import com.rose.gateway.minecraft.commands.arguments.ConfigNameArgs
import com.rose.gateway.minecraft.commands.framework.data.TabCompletionContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ConfigCompleter : KoinComponent {
    private val configStringMap: ConfigurationStringMap by inject()

    @Suppress("UNUSED_PARAMETER")
    fun configNameArgsNameCompletion(context: TabCompletionContext<ConfigNameArgs>): List<String> =
        configStringMap.allStrings()

    @Suppress("UNUSED_PARAMETER")
    fun configItemArgsNameCompletion(context: TabCompletionContext<ConfigItemArgs>): List<String> =
        configStringMap.allStrings()
}
