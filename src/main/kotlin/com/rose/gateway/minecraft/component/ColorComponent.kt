package com.rose.gateway.minecraft.component

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.primaryColor
import com.rose.gateway.config.extensions.secondaryColor
import com.rose.gateway.config.extensions.tertiaryColor
import com.rose.gateway.config.extensions.warningColor
import net.kyori.adventure.text.Component
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides functions that create colored [Component]s based on the plugin config
 */
object ColorComponent : KoinComponent {
    private val config: PluginConfig by inject()

    /**
     * Creates a text [Component] with the configured primary color
     *
     * @param text The text to create the [Component] with
     * @return The colored [Component]
     */
    fun primary(text: String): Component {
        return Component.text(text, config.primaryColor())
    }

    /**
     * Creates a text [Component] with the configured secondary color
     *
     * @param text The text to create the [Component] with
     * @return The colored [Component]
     */
    fun secondary(text: String): Component {
        return Component.text(text, config.secondaryColor())
    }

    /**
     * Creates a text [Component] with the configured tertiary color
     *
     * @param text The text to create the [Component] with
     * @return The colored [Component]
     */
    fun tertiary(text: String): Component {
        return Component.text(text, config.tertiaryColor())
    }

    /**
     * Creates a text [Component] with the configured warning color
     *
     * @param text The text to create the [Component] with
     * @return The colored [Component]
     */
    fun warning(text: String): Component {
        return Component.text(text, config.warningColor())
    }
}
