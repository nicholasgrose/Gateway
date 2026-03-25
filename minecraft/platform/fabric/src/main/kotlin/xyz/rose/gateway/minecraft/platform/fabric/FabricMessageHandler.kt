package xyz.rose.gateway.minecraft.platform.fabric

import net.kyori.adventure.text.Component
import net.minecraft.network.chat.PlayerChatMessage
import net.minecraft.server.level.ServerPlayer

class FabricMessageHandler {
    companion object {
        fun processMessage(fabricMessage: PlayerChatMessage, sender: ServerPlayer) {
            val adventureMessage = Component.text("<${sender.displayName.string}> ${fabricMessage.signedBody.content}")
        }

        fun processMessage(fabricMessage: net.minecraft.network.chat.Component) {
            val adventureMessage = Component.text(fabricMessage.string)
        }
    }
}
