package com.rose.gateway.shared.collections.builders

fun <T> dequeOf(): ArrayDeque<T> {
    return ArrayDeque()
}

fun <T> dequeOf(vararg values: T): ArrayDeque<T> {
    return values.toCollection(ArrayDeque())
}

fun <T> dequeOf(values: Collection<T>): ArrayDeque<T> {
    return ArrayDeque(values)
}
