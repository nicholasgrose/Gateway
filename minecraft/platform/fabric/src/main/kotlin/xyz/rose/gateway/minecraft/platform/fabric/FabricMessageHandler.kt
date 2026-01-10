package xyz.rose.gateway.minecraft.platform.fabric

import net.kyori.adventure.text.Component
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.message.SignedMessage
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class FabricMessageHandler {
    companion object {
        fun processMessage(fabricMessage: SignedMessage, sender: ServerPlayerEntity) {
            val adventureMessage = Component.text("<${sender.displayName?.string}> ${fabricMessage.content.string}")
        }

        fun processMessage(fabricMessage: Text) {
            val adventureMessage = Component.text(fabricMessage.string)
        }
    }
}
