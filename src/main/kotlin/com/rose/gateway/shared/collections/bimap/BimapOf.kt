package com.rose.gateway.shared.collections.bimap

/**
 * Creates an empty bimap
 *
 * @param V1 The first type to place in the map
 * @param V2 The second type to place in the map
 * @return The empty bimap
 */
fun <V1, V2> bimapOf(): Bimap<V1, V2> = Bimap<V1, V2>()

/**
 * Create a bimap for a list of value pairs
 *
 * @param V1 The first type to place in the map
 * @param V2 The second type to place in the map
 * @param pairs The value pairs to add to the bimap
 * @return A filled bimap
 */
fun <V1, V2> bimapOf(vararg pairs: Pair<V1, V2>): Bimap<V1, V2> {
    val bimap = bimapOf<V1, V2>()

    for (pair in pairs) {
        bimap[pair.first] = pair.second
    }

    return bimap
}
