package com.rose.gateway.minecraft.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration

/**
 * Appends two [Component]s.
 *
 * @param secondComponent The [Component] to append to the first.
 * @return The first [Component] with the second appended.
 */
operator fun Component.plus(secondComponent: Component): Component {
    return this.append(secondComponent)
}

/**
 * Joins multiple [Component]s into one without separators.
 *
 * @param components The [Component]s to combine.
 * @return The new, joined [Component].
 */
fun join(vararg components: Component): Component = Component.join(
    JoinConfiguration.noSeparators(),
    *components
)

/**
 * Joins multiple [Component]s into one without separators.
 *
 * @param components The [Component]s to combine.
 * @return The new, joined [Component].
 */
fun join(components: Collection<Component>): Component = Component.join(
    JoinConfiguration.noSeparators(),
    components
)

/**
 * Joins multiple [Component]s into one with new lines as separators.
 *
 * @param components The [Component]s to combine.
 * @return The new, joined [Component].
 */
fun joinNewLine(vararg components: Component): Component = Component.join(
    JoinConfiguration.newlines(),
    *components
)

/**
 * Joins multiple [Component]s into one with new lines as separators.
 *
 * @param components The [Component]s to combine.
 * @return The new, joined [Component].
 */
fun joinNewLine(components: Collection<Component>): Component = Component.join(
    JoinConfiguration.newlines(),
    components
)

/**
 * Joins multiple [Component]s into one with spaces as separators.
 *
 * @param components The [Component]s to combine.
 * @return The new, joined [Component].
 */
fun joinSpace(vararg components: Component): Component = Component.join(
    JoinConfiguration.separator(Component.text(" ")),
    *components
)
