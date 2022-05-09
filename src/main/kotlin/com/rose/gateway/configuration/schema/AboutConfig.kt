package com.rose.gateway.configuration.schema

import com.rose.gateway.bot.extensions.about.AboutExtension
import com.rose.gateway.configuration.markers.CommonExtensionConfig
import com.rose.gateway.configuration.markers.ConfigItem

class AboutConfig(
    enabled: Boolean
) : CommonExtensionConfig<AboutConfig>(AboutExtension.extensionName()) {
    @ConfigItem("Whether the extension is enabled.")
    var enabled = enabled
        set(value) {
            field = value
            modifyExtensionLoadedStatus(value)
        }
}
