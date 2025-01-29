# Overview

## Platforms, Plugins, and Capabilities

The Gateway application uses various abstractions as ways to group related functionality and ensure portable logic
between applications.
The primary abstractions used are platforms, plugins, and capabilities.

## Platforms

Platforms are "the things that connect to Gateway".
They could also be thought of as "third parties".

### Platform Examples

* Minecraft
* Discord

### Platform Logic

```mermaid
---
title: Opening Platform Connection
---
sequenceDiagram
    box Platform
        participant Pla as Platform
        participant Plu as Plugin
    end
    box Gateway
        participant G as Gateway Application
        participant P as Platform Registry
        participant R as Plugin Registry
        participant C as Capability Registry
    end

    Pla ->>+ G: Open connection
    G ->>+ P: Register platform
    alt Platform registers plugins?
        P ->>+ R: Register plugins
        R ->>+ Pla: Get plugin constructors
        Pla -->>- R: Plugin constructors
        loop Per platform plugin
            R ->>+ Plu: Create plugin
            Plu -->>- R: Plugin created
            alt Plugin registers capabilities?
                R ->>+ C: Register capabilities
                C ->>+ Plu: Get capabilities
                Plu -->>- C: Register capabilities
                C -->>- R: Capabilities registered
            end
        end
        R -->>- P: Plugins registered
    end
    P -->>- G: Platform registered
    G -->>- Pla: Connection opened
```

```mermaid
---
title: Closing Platform Connection
---
sequenceDiagram
    box Platform
        participant Pla as Platform
        participant Plu as Plugin
    end
    box Gateway
        participant G as Gateway Application
        participant P as Platform Registry
        participant R as Plugin Registry
        participant C as Capability Registry
    end

    Pla ->>+ G: Close connection
    G ->>+ P: Deregister platform
    alt Platform registers plugins?
        P ->>+ R: Deregister plugins
        loop Per plugin
            alt Plugin registers capabilities?
                R ->>+ C: Deregister capabilities
                C -->>- R: Capabilities unregistered
            end
        end
        R -->>- P: Plugins unregistered
    end
    P -->>- G: Platform unregistered
    G -->>- Pla: Connection closed
```

## Capabilities

If platforms are "what are we connecting to", capabilities are "what can we do with this connection".
Capabilities are how we access platform-specific as well as general functionality.

### Capability Examples

* Getting the list of online players in Minecraft
* Sending a message
* Parsing a message from a particular platform

## Plugins

Plugins are less directly tied to the concept of a platform.
Plugins group related pieces of functionality together for ease of organization or reasoning.

### Plugin Examples

* Minecraft whitelist functionality
* Handling of messages from a particular platform

## Organization

### Gateway App

#### Overall

```mermaid
classDiagram
    class Gateway {
        <<interface>>
        +connect(platform: Platform)
        +disconnect(platform: Platform)
    }

    Gateway *-- GatewayConfig
    Gateway *-- PlatformRegistry
    Gateway *-- PluginRegistry
    Gateway *-- CapabilityRegistry

    class GatewayConfig {
        <<interface>>
        +List~Platform~ platforms
    }

    GatewayConfig o-- GatewayPlatformConfig

    class GatewayPlatformConfig {
        <<interface>>
    }

    class Platform {
        <<interface>>
    }

    Platform --> RegistryItem
    PlatformRegistry *-- Platform

    class PlatformRegistry {
        <<interface>>
        +register(platform: Platform): PlatformRegistrationData
        +deregister(platform: Platform)
    }

    class Plugin {
        <<interface>>
    }

    Plugin --> RegistryItem
    PluginRegistry *-- Plugin

    class PluginRegistry {
        <<interface>>
        +register(plugin: Plugin): PluginRegistrationData
        +deregister(plugin: Plugin)
    }

    class Capability {
        <<interface>>
    }

    Capability --> RegistryItem
    CapabilityRegistry *-- Capability

    class CapabilityRegistry {
        <<interface>>
        +register(capability: Capability): CapabilityRegistrationData
        +deregister()
    }

    PlatformRegistry <|-- Registry~Platform~
    PluginRegistry <|-- Registry~Plugin~
    CapabilityRegistry <|-- Registry~Capability~

    class Registry~I~ {
        <<interface>>
        +register(item: Registrar~I~)
        +register(item: I)
        +deregister(item: I)
    }

    Registry~I~ o-- RegistryItem
    Registry~I~ -- Registrar~I~

    class RegistryItem {
        <<interface>>
    }

    class Registrar~I~ {
        <<interface>>
        +registrations(): List~I~
    }
```

#### Generic Capabilities

```mermaid
classDiagram
    SendMessageCapability <|-- Capability
    MessageEventCapability <|-- Capability
    AllowlistReadCapability <|-- Capability
    AllowlistWriteCapability <|-- Capability
    PerformanceReadCapability <|-- Capability
    VersionInfoReadCapability <|-- Capability
    OnlineCountCapability <|-- Capability
    ConnectionInfoReadCapability <|-- Capability

    class SendMessageCapability {
        <<interface>>
    }
    class MessageEventCapability {
        <<interface>>
    }
    class AllowlistReadCapability {
        <<interface>>
    }
    class AllowlistWriteCapability {
        <<interface>>
    }
    class PerformanceReadCapability {
        <<interface>>
    }
    class VersionInfoReadCapability {
        <<interface>>
    }
    class OnlineCountCapability {
        <<interface>>
    }
    class ConnectionInfoReadCapability {
        <<interface>>
    }
```

### Discord Platform

#### Discord Config
```mermaid
classDiagram
    class DiscordConfig {
        <<interface>>
        +String token
        +BotConfig bot
    }

    DiscordConfig --> GatewayPlatformConfig
    DiscordConfig *-- BotConfig

    class BotConfig {
        <<interface>>
        +List~String~ channels
        +ExtensionConfig extensions
    }

    BotConfig *-- ExtensionConfig

    class ExtensionConfig {
        <<interface>>
        +AboutConfig about
        +ChatConfig chat
        +ConnectionConfig ip
        +PlayerListConfig list
        +PerformanceConfig performance
        +AllowlistConfig allowlist
    }

    ExtensionConfig *-- AboutConfig
    ExtensionConfig *-- ChatConfig
    ExtensionConfig *-- ConnectionConfig
    ExtensionConfig *-- PlayerListConfig
    ExtensionConfig *-- PerformanceConfig
    ExtensionConfig *-- AllowlistConfig

    class AboutConfig {
        <<interface>>
    }

    class ChatConfig {
        <<interface>>
        +Boolean showRoleColor
    }

    class ConnectionConfig {
        <<interface>>
        +String displayIp
    }

    class PlayerListConfig {
        <<interface>>
        +Number playersPerPage
    }

    class PerformanceConfig {
        <<interface>>
    }

    class AllowlistConfig {
        <<interface>>
        +Number playersPerPage
    }

    AboutConfig --> BaseExtensionConfig
    ChatConfig --> BaseExtensionConfig
    ConnectionConfig --> BaseExtensionConfig
    PlayerListConfig --> BaseExtensionConfig
    PerformanceConfig --> BaseExtensionConfig
    AllowlistConfig --> BaseExtensionConfig

    class BaseExtensionConfig {
        <<interface>>
        +Boolean enabled
    }
```

#### Overall Discord Platform
```mermaid
classDiagram
    class DiscordPlatform {
        <<interface>>
    }

    class KordexBot {
        <<interface>>
    }

    KordexBot <|-- DiscordPlatform
    KordexBot *-- KordexMessagePlugin
    KordexBot *-- KordexAllowlistPlugin
    KordexBot *-- KordexPerformancePlugin
    KordexBot *-- KordexAboutPlugin
    KordexBot *-- KordexCountingPlugin
    KordexBot *-- KordexConnectPlugin

    class KordexMessagePlugin {
        <<interface>>
    }

    KordexMessagePlugin --> SendMessageCapability
    KordexMessagePlugin *-- KordexMessageEventCapability

    class KordexMessageEventCapability {
        <<interface>>
    }

    KordexMessageEventCapability <|-- MessageEventCapability

    class KordexAllowlistPlugin {
        <<interface>>
    }

    KordexAllowlistPlugin --> AllowlistReadCapability
    KordexAllowlistPlugin --> AllowlistWriteCapability

    class KordexPerformancePlugin {
        <<interface>>
    }

    KordexPerformancePlugin --> PerformanceReadCapability

    class KordexAboutPlugin {
        <<interface>>
    }

    KordexAboutPlugin --> VersionInfoReadCapability

    class KordexCountingPlugin {
        <<interface>>
    }

    KordexCountingPlugin --> OnlineCountCapability

    class KordexConnectPlugin {
        <<interface>>
    }

    KordexConnectPlugin --> ConnectionInfoReadCapability
```

### Minecraft Platforms

#### Minecraft Config
```mermaid
classDiagram
    class MinecraftConfig {
        <<interface>>
    }

    MinecraftConfig --> GatewayPlatformConfig
    MinecraftConfig *-- MinecraftColorConfig

    class MinecraftColorConfig {
        <<interface>>
        +String primary
        +String secondary
        +String tertiary
        +String warning
    }
```

#### Overall Minecraft Platform

```mermaid
classDiagram
    class MinecraftPlatform {
        <<interface>>
    }

    MinecraftPlatform *-- MinecraftWhitelistPlugin
    MinecraftPlatform *-- MinecraftTpsPlugin
    MinecraftPlatform *-- MinecraftVersionInfoPlugin
    MinecraftPlatform *-- MinecraftPlayerCountPlugin
    MinecraftPlatform *-- MinecraftChatPlugin

    class MinecraftWhitelistPlugin {
        <<interface>>
    }

    MinecraftWhitelistPlugin *-- MinecraftWhitelistReadCapability
    MinecraftWhitelistPlugin *-- MinecraftWhitelistWriteCapability
    MinecraftWhitelistReadCapability <|-- AllowlistReadCapability
    MinecraftWhitelistWriteCapability <|-- AllowlistWriteCapability

    class MinecraftWhitelistReadCapability {
        <<interface>>
    }

    class MinecraftWhitelistWriteCapability {
        <<interface>>
    }

    class MinecraftTpsPlugin {
        <<interface>>
    }

    MinecraftTpsPlugin *-- MinecraftTpsReadCapability
    MinecraftTpsReadCapability <|-- PerformanceReadCapability

    class MinecraftTpsReadCapability {
        <<interface>>
    }

    class MinecraftVersionInfoPlugin {
        <<interface>>
    }

    MinecraftVersionInfoPlugin *-- MinecraftVersionInfoReadCapability
    MinecraftVersionInfoReadCapability <|-- VersionInfoReadCapability

    class MinecraftVersionInfoReadCapability {
        <<interface>>
    }

    class MinecraftPlayerCountPlugin {
        <<interface>>
    }

    MinecraftPlayerCountPlugin *-- MinecraftOnlinePlayerReadPlugin
    MinecraftOnlinePlayerReadPlugin <|-- OnlineCountCapability

    class MinecraftOnlinePlayerReadPlugin {
        <<interface>>
    }
    class MinecraftChatPlugin {
        <<interface>>
    }

    MinecraftChatPlugin --> SendMessageCapability
    MinecraftChatPlugin *-- MinecraftSendMessageCapability
    MinecraftChatPlugin *-- MinecraftChatMessageEventCapability
    MinecraftSendMessageCapability <|-- SendMessageCapability
    MinecraftChatMessageEventCapability <|-- MessageEventCapability

    class MinecraftSendMessageCapability {
        <<interface>>
    }

    class MinecraftChatMessageEventCapability {
        <<interface>>
    }
```

#### Paper Platform

```mermaid
classDiagram
    class MinecraftPaperPlatform {
        <<interface>>
    }

    MinecraftPaperPlatform --> MinecraftPlatform
```

#### Fabric Platform

```mermaid
classDiagram
    class MinecraftFabricPlatform {
        <<interface>>
    }

    MinecraftFabricPlatform --> MinecraftPlatform
```
