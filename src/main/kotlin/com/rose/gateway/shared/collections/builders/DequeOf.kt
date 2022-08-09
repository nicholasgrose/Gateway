package com.rose.gateway.shared.collections.builders

/**
 * Creates an [ArrayDeque] that contains the provided elements.
 *
 * @param ElementType The type of the elements in the [ArrayDeque].
 * @param elements The elements to put in the [ArrayDeque].
 * @return The new, filled [ArrayDeque].
 *
 * @see ArrayDeque
 */
fun <ElementType> dequeOf(vararg elements: ElementType): ArrayDeque<ElementType> {
    return elements.toCollection(ArrayDeque())
}
