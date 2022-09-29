package com.rose.gateway.discord.bot.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import org.koin.core.component.KoinComponent

/**
 * A Discord extension that can be disabled or enabled, as needed
 * In general, this should be used by companion objects because those are the most convenient way to have an instance
 * at startup and have access to the class constructor
 *
 * @constructor Create a toggleable extension
 */
interface ExtensionToggle : KoinComponent {
    /**
     * Gives the name of this extension
     *
     * @return The name as a String
     */
    fun extensionName(): String

    /**
     * Gives the constructor for the extension this toggles
     *
     * @return The Extension constructor
     */
    fun extensionConstructor(): () -> Extension

    /**
     * Gives whether the extension is currently enabled
     *
     * @return Whether the extension is enabled
     */
    fun isEnabled(): Boolean
}
