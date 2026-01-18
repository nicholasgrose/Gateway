package xyz.rose.gateway.core

import org.junit.jupiter.api.AfterEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import xyz.rose.gateway.core.capability.GatewayCapability
import xyz.rose.gateway.core.platform.GatewayPlatform
import xyz.rose.gateway.core.plugin.GatewayPlugin
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Tests for the various gateway module extension methods
 */
class GatewayModuleExtensionTest : KoinTest {

    interface TestPlatform : GatewayPlatform
    class TestPlatformImpl : TestPlatform {
        override fun connect() {}
        override fun disconnect() {}
    }

    interface TestPlugin : GatewayPlugin
    class TestPluginImpl : TestPlugin {
        override fun onEnable() {}
        override fun onDisable() {}
    }

    interface TestCapability : GatewayCapability
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

        assertNotNull(getKoin().get<GatewayPlatform>())
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

        assertNotNull(getKoin().get<GatewayCapability>())
        assertNotNull(getKoin().get<TestCapabilityImpl>())
    }
}
