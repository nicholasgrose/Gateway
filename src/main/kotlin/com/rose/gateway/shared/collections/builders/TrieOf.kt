package com.rose.gateway.shared.collections.builders

import com.rose.gateway.shared.collections.trie.Trie

fun trieOf(values: Collection<String>): Trie {
    return values.toCollection(Trie())
}
