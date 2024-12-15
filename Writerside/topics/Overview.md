# Overview

```mermaid
---
title: Classes
---
classDiagram
    class Gateway {
        +connectPlatform(platform: Platform)
        +disconnectPlatform(platform: Platform)
    }

    Gateway *-- GatewayPlatform

    namespace Platform {
        class GatewayPlatform {
            <<interface>>
            +connect()
            +disconnect()
        }
        class PluginRegistry {
            <<interface>>
            +loadPlugin(plugin: PlatformPlugin)
            +unloadPlugin(plugin: PlatformPlugin)
        }

        class PlatformPlugin {
            <<interface>>
            +enable()
            +disable()
        }

        class PlatformIntegration {
            <<interface>>
        }
    }

    GatewayPlatform *.. PluginRegistry
    PluginRegistry *.. PlatformPlugin
    PlatformIntegration ..|> PlatformPlugin

    namespace Social {
        class SocialPlatform {
            <<interface>>
        }

        class Discord {
            <<interface>>
        }

        class KordexBot {
        }

        class DiscordBot {
            <<interface>>
            +start()
            +stop()
        }

        class DiscordChatEventPlugin {
            <<interface>>
            +onChatReceived(chatInfo: DiscordChat)
        }

        class DiscordChat {
            <<interface>>
            +senderName(): String
            +channel(): String
            +message(): String
        }

        class DiscordIntegration~T~ {
            <<interface>>
        }

        class DiscordMessageConverter {
            <<interface>>
        }

        class DiscordMinecraftPlugin {
        }

        class DiscordMinecraftMessageConverter {
        }

        class DiscordStringParser {
        }

        class DiscordStringBuilder {
        }
    }

    SocialPlatform ..|> GatewayPlatform
    Discord ..|> SocialPlatform
    Discord *.. DiscordIntegration
    DiscordIntegration ..|> PlatformIntegration
    DiscordIntegration *.. DiscordMessageConverter
    Discord *.. DiscordStringBuilder
    DiscordMinecraftPlugin ..|> DiscordIntegration
    DiscordMinecraftPlugin *.. DiscordMinecraftMessageConverter
    DiscordMinecraftMessageConverter ..|> DiscordMessageConverter
    DiscordMinecraftMessageConverter *.. MinecraftStringBuilder
    DiscordBot ..|> Discord
    KordexBot ..|> DiscordBot
    KordexBot *.. DiscordChatEventPlugin
    DiscordChatEventPlugin ..|> PlatformPlugin
    DiscordChatEventPlugin .. DiscordChat

    namespace Game {
        class GameServer {
            <<interface>>
        }

        class MinecraftServer {
            <<interface>>
        }

        class MinecraftStringBuilder {
            <<interface>>
        }

        class AdventureStringBuilder {
        }

        class MinecraftInfoPlugin {
            <<interface>>
        }

        class MinecraftDiscordPlugin {
        }

        class MinecraftMessageConverter {
            <<interface>>
        }

        class DiscordAdventureConverter {
        }

        class AdventureParser {
        }

        class PaperServer {
        }

        class PaperServerPlugin {
        }

        class PaperInfoPlugin {
        }

        class FabricServer {
        }

        class FabricMod {
        }

        class FabricInfoPlugin {
        }
    }

    GameServer ..|> GatewayPlatform
    MinecraftServer ..|> GameServer
    MinecraftServer *.. MinecraftDiscordPlugin
    MinecraftServer *.. MinecraftStringBuilder
    AdventureStringBuilder ..|> MinecraftStringBuilder
    MinecraftInfoPlugin ..|> PlatformPlugin
    MinecraftDiscordPlugin ..|> PlatformIntegration
    MinecraftDiscordPlugin *.. DiscordAdventureConverter
    DiscordAdventureConverter ..|> MinecraftMessageConverter
    DiscordAdventureConverter *.. AdventureParser
    DiscordAdventureConverter *.. DiscordStringBuilder
    PaperServer ..|> MinecraftServer
    PaperServerPlugin *.. PaperServer
    PaperServer *.. PaperInfoPlugin
    PaperInfoPlugin ..|> MinecraftInfoPlugin
    FabricServer ..|> MinecraftServer
    FabricServer *.. FabricInfoPlugin
    FabricMod *.. FabricServer
    FabricInfoPlugin ..|> MinecraftInfoPlugin

    namespace Config {
        class GatewayConfig {
            <<interface>>
        }

        class YamlConfig {
        }
    }

    Gateway *.. GatewayConfig
    YamlConfig ..|> GatewayConfig
```
