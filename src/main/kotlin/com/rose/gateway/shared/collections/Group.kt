package com.rose.gateway.shared.collections

/**
 * Groups the elements of a collection into lists of the given size
 *
 * @param T The type of the elements in the collection
 * @param groupSize The size of the groups to create
 * @return The resultant element groups
 */
fun <T : Any> Collection<T>.group(groupSize: Int): List<List<T>> {
    val groups = mutableListOf<MutableList<T>>()

    for ((index, element) in this.withIndex()) {
        if (index % groupSize == 0) {
            groups.add(mutableListOf(element))
            continue
        }

        val subsetIndex = index / groupSize
        groups[subsetIndex].add(element)
    }

    return groups
}
