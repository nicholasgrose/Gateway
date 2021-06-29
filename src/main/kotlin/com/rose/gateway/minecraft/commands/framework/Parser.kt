package com.rose.gateway.minecraft.commands.framework

interface Parser<T> {
    fun fromString(string: String): T?
    fun getName(): String
}
