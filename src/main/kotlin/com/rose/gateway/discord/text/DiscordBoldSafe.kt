package com.rose.gateway.discord.text

fun String.discordBoldSafe(): String {
    return this.replace("**", "\\**")
}
