package com.rose.gateway.shared.discord

fun String.discordBoldSafe(): String {
    return this.replace("**", "\\**")
}
