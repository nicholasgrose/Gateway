package com.rose.gateway.config.markers

@Target(AnnotationTarget.PROPERTY)
annotation class ConfigItem(
    val description: String = "NO DESCRIPTION"
)
