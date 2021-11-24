package com.rose.gateway.shared.collections.trie

class Trie : MutableSet<String> {
    private val rootNode = TrieNode("", null)
    override var size = 0

    private fun isLastIndexOfString(value: String, index: Int): Boolean {
        return index == value.length - 1
    }

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

    fun searchOrGetAll(prefix: String): List<String> {
        val searchResult = search(prefix)
        return searchResult.ifEmpty { getAll() }
    }

    private fun search(prefix: String): List<String> {
        val startNode = followSearchString(prefix) ?: return listOf()
        return depthFirstSearch(startNode)
    }

    private fun followSearchString(prefix: String): TrieNode? {
        var currentNode = rootNode

        for (character in prefix) {
            val nextNode = currentNode.children[character] ?: return null
            currentNode = nextNode
        }

        return currentNode
    }

    private fun depthFirstSearch(startNode: TrieNode): MutableList<String> {
        val results = mutableListOf<String>()
        val nodesToSearch = ArrayDeque<TrieNode>()
        nodesToSearch.add(startNode)

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

    private fun getAll(): MutableList<String> {
        return depthFirstSearch(rootNode)
    }

    override fun remove(element: String): Boolean {
        val node = followSearchString(element) ?: return false

        if (!node.isTerminalNode) return false

        node.isTerminalNode = false
        pruneTrieNode(node)

        return true
    }

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
            } else false
            success = success || elementRemoved
        }

        return success
    }
}
