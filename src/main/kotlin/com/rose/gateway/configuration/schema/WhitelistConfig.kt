package com.rose.gateway.configuration.schema

import com.rose.gateway.bot.extensions.whitelist.WhitelistExtension
import com.rose.gateway.configuration.markers.CommonExtensionConfig
import com.rose.gateway.configuration.markers.ConfigItem

class WhitelistConfig(
    enabled: Boolean
) : CommonExtensionConfig<WhitelistConfig>(WhitelistExtension.extensionName()) {
    @ConfigItem("Whether the extension is enabled.")
    var enabled = enabled
        set(value) {
            field = value
            modifyExtensionLoadedStatus(value)
        }
}
