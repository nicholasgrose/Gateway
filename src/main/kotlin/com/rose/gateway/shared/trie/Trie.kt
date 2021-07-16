package com.rose.gateway.shared.trie

class Trie {
    private val rootNode = TrieNode("")

    fun insertString(stringToInsert: String) {
        var currentNode = rootNode

        for (index in stringToInsert.indices) {
            val nodeForCharacter = getOrCreateChildNode(currentNode, stringToInsert, index)

            if (isLastIndexOfString(stringToInsert, index)) nodeForCharacter.isTerminalNode = true

            currentNode = nodeForCharacter
        }
    }

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
            val newNode = TrieNode(currentSubstring)
            trieNode.children[currentCharacter] = newNode
            newNode
        }
    }

    fun searchForStringsWithPrefix(prefix: String): List<String> {
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

    private fun depthFirstSearch(startNode: TrieNode): List<String> {
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
}
