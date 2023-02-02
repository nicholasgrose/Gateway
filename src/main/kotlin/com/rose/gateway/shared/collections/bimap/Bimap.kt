package com.rose.gateway.shared.collections.bimap

/**
 * A 2-directional map for efficient accesses of keys and values
 *
 * @param V1 The type of the first value to map
 * @param V2 The type of the second value to map
 * @constructor Create an empty bimap
 */
class Bimap<V1, V2> {
    private val direct = mutableMapOf<V1, V2>()
    private val inverse = mutableMapOf<V2, V1>()

    /**
     * Gets the value associated with the key
     *
     * @param key The key to the value
     * @return The associated value or null if none exists
     */
    @JvmName("getV1")
    operator fun get(key: V1): V2? = direct[key]

    /**
     * Gets the value associated with the key
     *
     * @param key The key to the value
     * @return The associated value or null if none exists
     */
    @JvmName("getV2")
    operator fun get(key: V2): V1? = inverse[key]

    /**
     * Add a key and its value
     *
     * @param key The key to add
     * @param value The value to add
     */
    @JvmName("setV1")
    operator fun set(key: V1, value: V2) {
        direct[key] = value
        inverse[value] = key
    }

    /**
     * Add a key and its value
     *
     * @param key The key to add
     * @param value The value to add
     */
    @JvmName("setV2")
    operator fun set(key: V2, value: V1) {
        direct[value] = key
        inverse[key] = value
    }
}
