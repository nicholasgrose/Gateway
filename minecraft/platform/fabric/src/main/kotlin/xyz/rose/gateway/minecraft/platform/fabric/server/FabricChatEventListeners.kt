package xyz.rose.gateway.minecraft.platform.fabric.server

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import xyz.rose.gateway.minecraft.platform.fabric.FabricMessageHandler

class FabricChatEventListeners {

    companion object {
        fun addChatListeners() {
            ServerMessageEvents.CHAT_MESSAGE.register { message, sender, _ ->
                FabricMessageHandler.processMessage(message, sender)
            }
            ServerMessageEvents.GAME_MESSAGE.register { _, message, _ ->
                FabricMessageHandler.processMessage(message)
            }
        }
    }
}
