package xyz.rose.gateway.core

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import xyz.rose.gateway.core.capability.GatewayCapability
import xyz.rose.gateway.core.platform.GatewayPlatform
import xyz.rose.gateway.core.platform.GatewayPlatformProvider
import xyz.rose.gateway.core.plugin.GatewayPlugin
import kotlin.test.Test

/**
 * Tests for [EmbeddedGatewayApp]
 */
class EmbeddedGatewayAppTest : KoinTest {

    private val logger = mockk<io.github.oshai.kotlinlogging.KLogger>(relaxed = true)
    private val platformProvider1 = mockk<GatewayPlatformProvider>()
    private val platform1 = mockk<GatewayPlatform>(relaxed = true)
    private val capability1 = mockk<GatewayCapability>(relaxed = true)
    private val plugin1 = mockk<GatewayPlugin>(relaxed = true)

    private val platformProvider2 = mockk<GatewayPlatformProvider>()
    private val platform2 = mockk<GatewayPlatform>(relaxed = true)
    private val capability2 = mockk<GatewayCapability>(relaxed = true)
    private val plugin2 = mockk<GatewayPlugin>(relaxed = true)

    private val runtimeModule1: Module = module {
        single(named("platform1")) { platform1 }.bindPlatform()
        single(named("capability1")) { capability1 }.bindCapability()
        single(named("plugin1")) { plugin1 }.bindPlugin()
    }

    private val runtimeModule2: Module = module {
        single(named("platform2")) { platform2 }.bindPlatform()
        single(named("capability2")) { capability2 }.bindCapability()
        single(named("plugin2")) { plugin2 }.bindPlugin()
    }

    @BeforeEach
    fun setup() {
        every { platformProvider1.createRuntimeModule() } returns runtimeModule1
        every { platformProvider2.createRuntimeModule() } returns runtimeModule2

        startKoin {
            modules(module {
                single(named("p1")) { platformProvider1 }
                single(named("p2")) { platformProvider2 }
            })
        }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test start enables all components from multiple platforms`() {
        val app = EmbeddedGatewayApp(logger)
        app.start()

        verify { platform1.connect() }
        verify { capability1.onEnable() }
        verify { plugin1.onEnable() }

        verify { platform2.connect() }
        verify { capability2.onEnable() }
        verify { plugin2.onEnable() }
    }

    @Test
    fun `test stop disables all components from multiple platforms`() {
        val app = EmbeddedGatewayApp(logger)
        app.start()
        app.stop()

        verify { plugin1.onDisable() }
        verify { capability1.onDisable() }
        verify { platform1.disconnect() }

        verify { plugin2.onDisable() }
        verify { capability2.onDisable() }
        verify { platform2.disconnect() }
    }
}
