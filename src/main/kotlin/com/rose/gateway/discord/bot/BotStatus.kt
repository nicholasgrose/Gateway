package com.rose.gateway.discord.bot

enum class BotStatus(val status: String, var reason: String = "") {
    NOT_STARTED("Not Started"),
    STARTING("Starting"),
    RUNNING("Running"),
    STOPPING("Stopping"),
    STOPPED("Stopped");

    infix fun because(newReason: String): BotStatus {
        reason = newReason

        return this
    }
}
