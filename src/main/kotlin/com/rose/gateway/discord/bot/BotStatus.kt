package com.rose.gateway.discord.bot

/**
 * The status of the Discord bot and why it is in that state
 *
 * @property status The name of the current status
 * @property reason Why the bot is in that status
 * @constructor Creates a bot status with the given info
 */
enum class BotStatus(
    val status: String,
    var reason: String = "",
) {
    /**
     * Represents that the bot has not yet started
     *
     * @constructor Create a "not started" status
     */
    NOT_STARTED("Not Started"),

    /**
     * Represents that the bot has started
     *
     * @constructor Create a "started" status
     */
    STARTING("Starting"),

    /**
     * Represents that the bot is running
     *
     * @constructor Create a "running" status
     */
    RUNNING("Running"),

    /**
     * Represents that the bot is stopping
     *
     * @constructor Create a "stopping" status
     */
    STOPPING("Stopping"),

    /**
     * Represents that the bot has stopped
     *
     * @constructor Create a "stopped" status
     */
    STOPPED("Stopped"),
    ;

    /**
     * Assign a reason to the bot's status
     *
     * @param reason The reason why it is in its current status
     * @return The modified status
     */
    infix fun because(reason: String): BotStatus {
        this.reason = reason

        return this
    }
}
