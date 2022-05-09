package com.rose.gateway.configuration.schema

import com.rose.gateway.bot.extensions.list.ListExtension
import com.rose.gateway.configuration.markers.CommonExtensionConfig
import com.rose.gateway.configuration.markers.ConfigItem

class ListConfig(
    enabled: Boolean
) : CommonExtensionConfig<ListConfig>(ListExtension.extensionName()) {
    @ConfigItem("Whether the extension is enabled.")
    var enabled = enabled
        set(value) {
            field = value
            modifyExtensionLoadedStatus(value)
        }
}
