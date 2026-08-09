package xyz.rose.gateway.core

import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import xyz.rose.gateway.core.capability.Capability
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.plugin.GatewayPlugin
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Tests for the various gateway module extension methods
 */
class GatewayModuleExtensionTest : KoinTest {

    interface TestPlatform : Platform
    class TestPlatformImpl : TestPlatform {
        override val id: String = "test"
        override val info: xyz.rose.gateway.core.platform.PlatformInfo = mockk()
        override val capabilities: List<Capability> = emptyList()
        override suspend fun connect() {}
        override suspend fun disconnect() {}
    }

    interface TestPlugin : GatewayPlugin
    class TestPluginImpl : TestPlugin {
        override fun onEnable() {}
        override fun onDisable() {}
    }

    interface TestCapability : Capability, Capability.Enableable, Capability.Disableable
    class TestCapabilityImpl : TestCapability {
        override fun onEnable() {}
        override fun onDisable() {}
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test bindPlatform`() {
        startKoin {
            modules(module {
                single { TestPlatformImpl() }.bindPlatform()
            })
        }

        assertNotNull(getKoin().get<Platform>())
        assertNotNull(getKoin().get<TestPlatformImpl>())
    }

    @Test
    fun `test bindPlugin`() {
        startKoin {
            modules(module {
                single { TestPluginImpl() }.bindPlugin()
            })
        }

        assertNotNull(getKoin().get<GatewayPlugin>())
        assertNotNull(getKoin().get<TestPluginImpl>())
    }

    @Test
    fun `test bindCapability`() {
        startKoin {
            modules(module {
                single { TestCapabilityImpl() }.bindCapability()
            })
        }

        assertNotNull(getKoin().get<Capability>())
        assertNotNull(getKoin().get<TestCapabilityImpl>())
    }
}
