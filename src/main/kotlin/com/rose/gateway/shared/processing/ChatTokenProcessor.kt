package com.rose.gateway.shared.processing

import guru.zoroark.lixy.LixyToken

interface ChatTokenProcessor<T> {
    fun regexPattern(): String
    suspend fun process(token: LixyToken): T
}
