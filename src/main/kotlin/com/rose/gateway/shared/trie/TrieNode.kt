package com.rose.gateway.shared.trie

import java.util.*

data class TrieNode(
    val nodeValue: String,
    var isTerminalNode: Boolean = false,
    val children: SortedMap<Char, TrieNode> = sortedMapOf(Comparator.reverseOrder())
)
