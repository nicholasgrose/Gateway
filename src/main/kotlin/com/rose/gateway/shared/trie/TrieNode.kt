package com.rose.gateway.shared.trie

import java.util.*

class TrieNode(
    val nodeValue: String,
    val parent: TrieNode?,
    var isTerminalNode: Boolean = false,
    val children: SortedMap<Char, TrieNode> = sortedMapOf(Comparator.reverseOrder())
)
