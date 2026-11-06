package xyz.rose.gateway.core

import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import xyz.rose.gateway.core.capability.Capability
import xyz.rose.gateway.core.capability.GatewayMessage
import xyz.rose.gateway.core.capability.GatewayMessageData
import xyz.rose.gateway.core.capability.GatewayMessageResponse
import xyz.rose.gateway.core.capability.MessageEnricher
import xyz.rose.gateway.core.capability.MessageListener
import xyz.rose.gateway.core.capability.MessageMetadata
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.plugin.GatewayPlugin
import kotlin.reflect.KClass
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
        override suspend fun connect() = Unit
        override suspend fun disconnect() = Unit
    }

    interface TestPlugin : GatewayPlugin
    class TestPluginImpl : TestPlugin {
        override fun onEnable() = Unit
        override fun onDisable() = Unit
    }

    interface TestCapability : Capability, Capability.Enableable, Capability.Disableable
    class TestCapabilityImpl : TestCapability {
        override fun onEnable() = Unit
        override fun onDisable() = Unit
    }

    data class DummyData(val text: String) : GatewayMessageData
    data class DummyMetadata(val info: String) : MessageMetadata
    data class DummyResponse(val success: Boolean) : GatewayMessageResponse

    class TestEnricherImpl : MessageEnricher<DummyData> {
        override val dataType: KClass<DummyData> = DummyData::class
        override suspend fun enrich(message: GatewayMessage<DummyData>): MessageMetadata = DummyMetadata("dummy")
    }

    class TestListenerImpl : MessageListener<DummyData, DummyResponse> {
        override val dataType: KClass<DummyData> = DummyData::class
        override val response: KClass<DummyResponse> = DummyResponse::class
        override suspend fun onMessage(message: GatewayMessage<DummyData>): DummyResponse = DummyResponse(true)
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

    @Test
    fun `test bindEnricher`() {
        startKoin {
            modules(module {
                single { TestEnricherImpl() }.bindEnricher()
            })
        }

        assertNotNull(getKoin().get<Capability>())
        assertNotNull(getKoin().get<MessageEnricher<*>>())
        assertNotNull(getKoin().get<TestEnricherImpl>())
    }

    @Test
    fun `test bindListener`() {
        startKoin {
            modules(module {
                single { TestListenerImpl() }.bindListener()
            })
        }

        assertNotNull(getKoin().get<Capability>())
        assertNotNull(getKoin().get<MessageListener<*, *>>())
        assertNotNull(getKoin().get<TestListenerImpl>())
    }
}
