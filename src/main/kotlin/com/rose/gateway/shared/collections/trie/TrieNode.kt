package com.rose.gateway.shared.collections.trie

import java.util.*

data class TrieNode(
    val nodeValue: String,
    val parent: TrieNode?,
    var isTerminalNode: Boolean = false,
    val children: SortedMap<Char, TrieNode> = sortedMapOf(Comparator.reverseOrder())
)
