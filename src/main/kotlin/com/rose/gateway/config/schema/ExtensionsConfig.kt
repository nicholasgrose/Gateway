package com.rose.gateway.config.schema

import com.rose.gateway.config.markers.ConfigItem
import com.rose.gateway.config.markers.ConfigObject
import kotlinx.serialization.Serializable

/**
 * Config for the Discord bot's extensions
 *
 * @property about Config for the about extension
 * @property chat Config for the chat extension
 * @property ip Config for the IP extension
 * @property list Config for the list extension
 * @property tps Config for the TPS extension
 * @property whitelist Config for the whitelist extension
 * @constructor Create an extension config
 */
@Serializable
data class ExtensionsConfig(
    @ConfigItem val about: AboutConfig,
    @ConfigItem val chat: ChatConfig,
    @ConfigItem val ip: IpConfig,
    @ConfigItem val list: ListConfig,
    @ConfigItem val tps: TpsConfig,
    @ConfigItem val whitelist: WhitelistConfig
) : ConfigObject
