package com.rose.gateway.shared.collections.builders

import com.rose.gateway.shared.collections.trie.Trie

/**
 * Creates a new [Trie] containing all the strings in another collection
 *
 * @param values The values to add to the [Trie]
 * @return The new [Trie], filled with the provided values
 *
 * @see Trie
 */
fun trieOf(values: Collection<String>): Trie {
    return values.toCollection(Trie())
}
