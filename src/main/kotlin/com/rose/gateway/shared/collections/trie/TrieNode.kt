package com.rose.gateway.shared.collections.trie

import java.util.SortedMap

/**
 * A single node of a [Trie]
 *
 * @property nodeValue The substring associated with this node
 * @property parent The next node up the [Trie], if any
 * @property isTerminalNode Whether this node represents a complete string contained in the [Trie]
 * @property children All [TrieNode]s below this node, accessible by the next character they represent
 * @constructor Creates a trie node
 *
 * @see Trie
 */
data class TrieNode(
    val nodeValue: String,
    val parent: TrieNode?,
    var isTerminalNode: Boolean = false,
    val children: SortedMap<Char, TrieNode> = sortedMapOf(Comparator.reverseOrder()),
)
