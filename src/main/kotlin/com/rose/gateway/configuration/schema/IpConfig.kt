package com.rose.gateway.configuration.schema

import com.rose.gateway.bot.extensions.ip.IpExtension
import com.rose.gateway.configuration.markers.CommonExtensionConfig
import com.rose.gateway.configuration.markers.ConfigItem

class IpConfig(
    enabled: Boolean,
    @ConfigItem("The IP that will be displayed by the IP command.")
    var displayIp: String
) : CommonExtensionConfig<IpConfig>(IpExtension.extensionName()) {
    @ConfigItem("Whether the extension is enabled.")
    var enabled = enabled
        set(value) {
            field = value
            modifyExtensionLoadedStatus(value)
        }
}
