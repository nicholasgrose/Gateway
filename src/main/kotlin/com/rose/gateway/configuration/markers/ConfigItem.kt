package com.rose.gateway.configuration.markers

@Target(AnnotationTarget.PROPERTY)
annotation class ConfigItem(
    val description: String = "NO DESCRIPTION"
)
