package com.rose.gateway.minecraft.component

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.primaryColor
import com.rose.gateway.config.access.secondaryColor
import com.rose.gateway.config.access.tertiaryColor
import com.rose.gateway.config.access.warningColor
import net.kyori.adventure.text.Component
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Contains functions for modifying components with configured plugin colors
 */
object Color : KoinComponent {
    private val config: PluginConfig by inject()

    /**
     * Apply the primary color to the component
     *
     * @return The colored component
     */
    public fun Component.primary(): Component = this.color(config.primaryColor())

    /**
     * Apply the secondary color to the component
     *
     * @return The colored component
     */
    public fun Component.secondary(): Component = this.color(config.secondaryColor())

    /**
     * Apply the tertiary color to the component
     *
     * @return The colored component
     */
    public fun Component.tertiary(): Component = this.color(config.tertiaryColor())

    /**
     * Apply the warning color to the component
     *
     * @return The colored component
     */
    public fun Component.warning(): Component = this.color(config.warningColor())
}
