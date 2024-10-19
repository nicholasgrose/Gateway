package com.rose.gateway.discord.text

/**
 * Changes a string so that it lacks bold formatting if sent in Discord
 *
 * @return The bold-safe string
 */
fun String.discordBoldSafe(): String = this.replace("**", "\\**")
