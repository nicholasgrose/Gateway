package com.rose.gateway.shared.discord

object StringModifiers {
    fun String.discordBoldSafe(): String {
        return this.replace("**", "\\**")
    }
}
