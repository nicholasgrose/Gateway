package com.rose.gateway.bot.extensions.list

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.rose.gateway.Logger
import com.rose.gateway.bot.message.MessageLifetime.respondWithLifetime
import com.rose.gateway.minecraft.server.ServerInfo

class ListExtension : Extension() {
    override val name: String = "list"

    override suspend fun setup() {
        command {
            name = "list"
            aliases = arrayOf("ls", "l")
            description = "Gives a list of all online players."

            action {
                Logger.log("${user?.username} requested player list!")
                val playerList = ServerInfo.getPlayerList()
                val response =
                    if (playerList.isEmpty()) "No players online."
                    else "Players online: $playerList"
                message.respondWithLifetime {
                    content = response
                }
            }
        }
    }
}
