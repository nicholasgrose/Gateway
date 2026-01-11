package xyz.rose.gateway.platform.paper

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.entity.Player

class PaperMessageHandler {
    companion object {
        fun processMessage(message: Component, sender: Player) {
            val messageText = PlainTextComponentSerializer.plainText().serialize(message)
            val adventureMessage = Component.text("<${sender.name}> $messageText")
        }
    }
}
