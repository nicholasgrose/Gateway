package com.rose.gateway.config.markers

/**
 * Marks a property as to be included as part of the config schema.
 *
 * @property description An optional description of the config item.
 * @constructor Creates a config item.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class ConfigItem(
    val description: String = "NO DESCRIPTION"
)
