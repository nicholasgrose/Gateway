package com.rose.gateway.minecraft.commands.framework

interface ArgumentConverter<T> {
    fun fromString(string: String): T?
    fun getName(): String
}
