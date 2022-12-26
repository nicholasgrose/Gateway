package com.rose.gateway.minecraft.component

import com.rose.gateway.minecraft.component.Color.primary
import com.rose.gateway.minecraft.component.Color.secondary
import com.rose.gateway.minecraft.component.Color.tertiary
import com.rose.gateway.minecraft.component.Color.warning
import net.kyori.adventure.text.Component

/**
 * Creates a [Component] for this string
 *
 * @return The created [Component]
 */
fun String.component(): Component = Component.text(this)

/**
 * Creates a [Component] in the configured primary color
 *
 * @return The created [Component]
 */
fun String.primaryComponent(): Component = this.component().primary()

/**
 * Creates a [Component] in the configured secondary color
 *
 * @return The created [Component]
 */
fun String.secondaryComponent(): Component = this.component().secondary()

/**
 * Creates a [Component] in the configured tertiary color
 *
 * @return The created [Component]
 */
fun String.tertiaryComponent(): Component = this.component().tertiary()

/**
 * Creates a [Component] in the configured warning color
 *
 * @return The created [Component]
 */
fun String.warningComponent(): Component = this.component().warning()
