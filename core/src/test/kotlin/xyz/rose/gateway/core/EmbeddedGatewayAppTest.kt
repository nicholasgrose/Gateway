package xyz.rose.gateway.core

import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.ScopeDSL
import org.koin.dsl.module
import org.koin.test.KoinTest
import xyz.rose.gateway.core.capability.Capability
import xyz.rose.gateway.core.capability.GatewayDispatcher
import xyz.rose.gateway.core.config.ConfigSchema
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.platform.PlatformProvider
import xyz.rose.gateway.core.platform.PlatformType
import xyz.rose.gateway.core.plugin.GatewayPlugin
import kotlin.test.Test

/**
 * Tests for [EmbeddedGatewayApp]
 */
class EmbeddedGatewayAppTest : KoinTest {

    interface FullCapability : Capability.Enableable, Capability.Disableable

    private val logger = mockk<io.github.oshai.kotlinlogging.KLogger>(relaxed = true)
    private val platform1 = mockk<Platform>(relaxed = true)
    private val capability1 = mockk<FullCapability>(relaxed = true)
    private val plugin1 = mockk<GatewayPlugin>(relaxed = true)

    private val platform2 = mockk<Platform>(relaxed = true)
    private val capability2 = mockk<FullCapability>(relaxed = true)
    private val plugin2 = mockk<GatewayPlugin>(relaxed = true)

    private val platformProvider1 = object : PlatformProvider {
        override fun ScopeDSL.createRuntimeModule() {
            scoped { platform1 }.bindPlatform()
            scoped { capability1 }.bindCapability()
            scoped { plugin1 }.bindPlugin()
        }
    }

    private val platformProvider2 = object : PlatformProvider {
        override fun ScopeDSL.createRuntimeModule() {
            scoped { platform2 }.bindPlatform()
            scoped { capability2 }.bindCapability()
            scoped { plugin2 }.bindPlugin()
        }
    }

    private lateinit var registry: GatewayRegistry

    @BeforeEach
    fun setup() {
        startKoin {
            modules(module {
                single { mockk<GatewayDispatcher>(relaxed = true) }
            })
        }
        val def1 = GatewayPlatformDefinition(
            PlatformType.MINECRAFT,
            "p1",
            mockk<ConfigSchema<Any>>(relaxed = true),
            platformProvider1
        )
        val def2 = GatewayPlatformDefinition(
            PlatformType.DISCORD,
            "p2",
            mockk<ConfigSchema<Any>>(relaxed = true),
            platformProvider2
        )
        registry = GatewayRegistry(listOf(def1, def2))
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test start enables all components from multiple platforms`() = runTest {
        val app = EmbeddedGatewayApp(logger, registry)
        app.start()

        coVerify { platform1.connect() }
        verify { capability1.onEnable() }
        verify { plugin1.onEnable() }

        coVerify { platform2.connect() }
        verify { capability2.onEnable() }
        verify { plugin2.onEnable() }
    }

    @Test
    fun `test stop disables all components from multiple platforms`() = runTest {
        val app = EmbeddedGatewayApp(logger, registry)
        app.start()
        app.stop()

        verify { plugin1.onDisable() }
        verify { capability1.onDisable() }
        coVerify { platform1.disconnect() }

        verify { plugin2.onDisable() }
        verify { capability2.onDisable() }
        coVerify { platform2.disconnect() }
    }
}
