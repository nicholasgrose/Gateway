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
    P ->>+ R: Register plugins

    loop Per platform plugin
        R ->>+ Plu: Create plugin
        Plu -->>- R: Plugin created
        R ->>+ C: Register capabilities
        C ->>+ Plu: Get capabilities
        Plu -->>- C: Register capabilities
        C -->>- R: Capabilities registered
    end

    R -->>- P: Plugin registration data
    Note over P: Registration data saved off
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

    Note over P: Using registration data
    P ->>+ R: Deregister plugins
    R -->>- P: Plugins unregistered
    P ->>+ C: Deregister capabilities
    C -->>- P: Capabilities unregistered

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

```mermaid
classDiagram
    class Gateway {
        <<interface>>
        +connect(platform: Platform)
        +disconnect(platform: Platform)
    }

    Gateway *--Config

    class Config {
        <<interface>>
        +load()
    }

    Gateway *-- PlatformRegistry
    Gateway *-- PluginRegistry
    Gateway *-- CapabilityRegistry

    class PlatformRegistry {
        <<interface>>
        +register(platform: Platform): PlatformRegistrationData
        +deregister(platform: Platform)
    }

    class PluginRegistry {
        <<interface>>
        +register(plugin: Plugin): PluginRegistrationData
        +deregister(plugin: Plugin)
    }

    class CapabilityRegistry {
        <<interface>>
        +register(capability: Capability): CapabilityRegistrationData
        +deregister()
    }

    PlatformRegistry <|-- Registry~Platform~
    PluginRegistry <|-- Registry~Plugin~
    CapabilityRegistry <|-- Registry~Capability~

    class Registry~T~ {
        <<interface>>
        +register(item: T): RegistrationData~T~
        +deregister(item: T)
    }

    Registry~T~ -- RegistrationData~T~

    class RegistrationData~T~ {
        <<interface>>
    }
```

### Discord Platform
```mermaid
classDiagram
    class Gateway {
        +connectPlatform(platform: Platform)
        +disconnectPlatform(platform: Platform)
    }
```

### Minecraft Platform
```mermaid
classDiagram
    class Gateway {
        +connectPlatform(platform: Platform)
        +disconnectPlatform(platform: Platform)
    }
```

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
