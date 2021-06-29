package com.rose.gateway.minecraft.commands.framework.converters

import com.rose.gateway.minecraft.commands.framework.ArgumentConverter

class StringArg(private val name: String) : ArgumentConverter<String> {
    override fun fromString(string: String): String {
        return string
    }

    override fun getName(): String {
        return name
    }
}
