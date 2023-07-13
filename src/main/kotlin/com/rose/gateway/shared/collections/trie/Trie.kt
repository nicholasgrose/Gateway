package com.rose.gateway.shared.collections.trie

import com.rose.gateway.shared.collections.builders.dequeOf

/**
 * A tree-based data structure in which each node in the tree represents a prefix string
 * It can thus be used to store and find strings which match some prefix
 *
 * @constructor Creates an empty Trie
 */
@Suppress("TooManyFunctions")
class Trie : MutableSet<String> {
    private val rootNode = TrieNode("", null)
    override var size = 0

    /**
     * Determines whether an index is the final index in a string
     *
     * @param value The string to check the last index of
     * @param index The index that might be the final index of the string
     * @return Whether the given index is the last index of the string
     */
    private fun isLastIndexOfString(value: String, index: Int): Boolean {
        return index == value.length - 1
    }

    /**
     * Finds an existing node by following the provided string starting at the current index
     * or creates it if it does not yet exist
     *
     * @param trieNode The node to check the children of
     * @param stringToInsert The string being inserted into the Trie
     * @param currentCharacterIndex The current index of the string being inserted
     * @return The matching node or a newly created node that does match
     */
    private fun getOrCreateChildNode(trieNode: TrieNode, stringToInsert: String, currentCharacterIndex: Int): TrieNode {
        val currentCharacter = stringToInsert[currentCharacterIndex]
        val matchingChild = trieNode.children[currentCharacter]

        return if (matchingChild != null) {
            matchingChild
        } else {
            val currentSubstring = stringToInsert.substring(0..currentCharacterIndex)
            val newNode = TrieNode(currentSubstring, trieNode)
            trieNode.children[currentCharacter] = newNode
            newNode
        }
    }

    /**
     * Searches for all strings with the given string as a prefix
     *
     * @param prefix The prefix string to search for
     * @return All matching strings
     */
    fun search(prefix: String): List<String> {
        val startNode = followSearchString(prefix) ?: return listOf()
        return depthFirstSearch(startNode)
    }

    /**
     * Gives the node at the end of a path, as defined by the prefix string, through the tree
     *
     * @param prefix The prefix string to follow through the tree
     * @return The node at the end of the defined path or null, if no such path can be followed
     */
    private fun followSearchString(prefix: String): TrieNode? {
        var currentNode = rootNode

        for (character in prefix) {
            val nextNode = currentNode.children[character] ?: return null
            currentNode = nextNode
        }

        return currentNode
    }

    /**
     * Conducts a depth first search for all full strings beneath a particular [TrieNode]
     *
     * @param startNode The node to start the search from
     * @return All complete strings in the trie beneath the provided node
     */
    private fun depthFirstSearch(startNode: TrieNode): MutableList<String> {
        val results = mutableListOf<String>()
        val nodesToSearch = dequeOf(startNode)

        while (!nodesToSearch.isEmpty()) {
            val currentNode = nodesToSearch.removeLast()

            if (currentNode.isTerminalNode) results.add(currentNode.nodeValue)

            for (node in currentNode.children.values) {
                nodesToSearch.add(node)
            }
        }

        return results
    }

    override fun contains(element: String): Boolean {
        return followSearchString(element)?.isTerminalNode ?: false
    }

    override fun containsAll(elements: Collection<String>): Boolean {
        return elements.all { contains(it) }
    }

    override fun isEmpty(): Boolean {
        return rootNode.children.isEmpty()
    }

    override fun add(element: String): Boolean {
        size += 1
        var currentNode = rootNode

        for (index in element.indices) {
            val nodeForCharacter = getOrCreateChildNode(currentNode, element, index)

            if (isLastIndexOfString(element, index)) nodeForCharacter.isTerminalNode = true

            currentNode = nodeForCharacter
        }

        return true
    }

    override fun addAll(elements: Collection<String>): Boolean {
        var success = false

        for (element in elements) {
            val elementAdded = add(element)
            success = success || elementAdded
        }

        return success
    }

    override fun clear() {
        rootNode.children.clear()
    }

    override fun iterator(): MutableIterator<String> {
        return getAll().iterator()
    }

    /**
     * Gets all the strings in the trie
     *
     * @return A list of all strings in the Trie
     */
    private fun getAll(): MutableList<String> {
        return depthFirstSearch(rootNode)
    }

    override fun remove(element: String): Boolean {
        val node = followSearchString(element)

        if (node == null || !node.isTerminalNode) return false

        node.isTerminalNode = false
        pruneTrieNode(node)

        return true
    }

    /**
     * Prunes a non-terminal trie node
     * by removing itself and all its non-terminal parents for which it is the only leaf
     *
     * @param node The node to prune
     */
    private fun pruneTrieNode(node: TrieNode) {
        if (node.parent != null && !node.isTerminalNode && node.children.isEmpty()) {
            val parent = node.parent
            parent.children.remove(node.nodeValue.last())
            pruneTrieNode(parent)
        }
    }

    override fun removeAll(elements: Collection<String>): Boolean {
        var success = false

        for (element in elements) {
            val elementRemoved = remove(element)
            success = success || elementRemoved
        }

        return success
    }

    override fun retainAll(elements: Collection<String>): Boolean {
        val contents = getAll()
        val keepSet = elements.toSet()
        var success = false

        for (element in contents) {
            val elementRemoved = if (!keepSet.contains(element)) {
                remove(element)
            } else {
                false
            }
            success = success || elementRemoved
        }

        return success
    }
}
