package com.rose.gateway.configuration.schema

import com.rose.gateway.bot.extensions.chat.ChatExtension
import com.rose.gateway.configuration.markers.CommonExtensionConfig
import com.rose.gateway.configuration.markers.ConfigItem

class ChatConfig(
    enabled: Boolean
) : CommonExtensionConfig<ChatConfig>(ChatExtension.extensionName()) {
    @ConfigItem("Whether the extension is enabled.")
    var enabled = enabled
        set(value) {
            field = value
            modifyExtensionLoadedStatus(value)
        }
}
