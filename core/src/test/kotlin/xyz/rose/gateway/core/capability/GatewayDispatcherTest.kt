package xyz.rose.gateway.core.capability

import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.ScopeDSL
import org.koin.test.KoinTest
import xyz.rose.gateway.core.GatewayRegistry
import xyz.rose.gateway.core.bindEnricher
import xyz.rose.gateway.core.bindListener
import xyz.rose.gateway.core.bindPlatform
import xyz.rose.gateway.core.config.ConfigSchema
import xyz.rose.gateway.core.platform.GatewayPlatformDefinition
import xyz.rose.gateway.core.platform.Platform
import xyz.rose.gateway.core.platform.PlatformProvider
import xyz.rose.gateway.core.platform.PlatformType
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GatewayDispatcherTest : KoinTest {

    data class TestData(val message: String) : GatewayMessageData
    data class OtherData(val value: Int) : GatewayMessageData

    data class TestMetadata(val note: String) : MessageMetadata

    data class TestResponse(val result: String) : GatewayMessageResponse

    class TestListener(
        override val dataType: KClass<TestData> = TestData::class,
        override val response: KClass<TestResponse> = TestResponse::class,
        private val responsePrefix: String = "handled"
    ) : MessageListener<TestData, TestResponse> {
        override suspend fun onMessage(message: GatewayMessage<TestData>): TestResponse {
            return TestResponse("$responsePrefix: ${message.data.message}")
        }
    }

    class TestEnricher : MessageEnricher<TestData> {
        override val dataType: KClass<TestData> = TestData::class
        override suspend fun enrich(message: GatewayMessage<TestData>): MessageMetadata {
            return TestMetadata("enriched: ${message.data.message}")
        }
    }

    private lateinit var registry: GatewayRegistry
    private lateinit var dispatcher: GatewayDispatcher
    private lateinit var client: PlatformGatewayClient
    private lateinit var platformDef1: GatewayPlatformDefinition<Any>
    private lateinit var platformDef2: GatewayPlatformDefinition<Any>

    @BeforeEach
    fun setup() {
        startKoin {}

        val provider1 = object : PlatformProvider {
            override fun ScopeDSL.createRuntimeModule() {
                scoped { mockk<Platform>(relaxed = true) }.bindPlatform()
                scoped { TestListener(responsePrefix = "p1") }.bindListener()
                scoped { TestEnricher() }.bindEnricher()
            }
        }

        val provider2 = object : PlatformProvider {
            override fun ScopeDSL.createRuntimeModule() {
                scoped { mockk<Platform>(relaxed = true) }.bindPlatform()
                scoped { TestListener(responsePrefix = "p2") }.bindListener()
            }
        }

        platformDef1 = GatewayPlatformDefinition(
            PlatformType.MINECRAFT,
            "server-1",
            mockk<ConfigSchema<Any>>(relaxed = true),
            provider1
        )
        platformDef2 = GatewayPlatformDefinition(
            PlatformType.DISCORD,
            "bot-1",
            mockk<ConfigSchema<Any>>(relaxed = true),
            provider2
        )

        val logger = mockk<io.github.oshai.kotlinlogging.KLogger>(relaxed = true)
        registry = GatewayRegistry(listOf(platformDef1, platformDef2))
        dispatcher = GatewayDispatcher(registry, logger)
        client = PlatformGatewayClient(platformDef1, dispatcher)
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test dispatch executes enrichers and aggregates listener responses`() = runTest {
        val responses: PlatformMap<TestResponse> = client.dispatch(TestData("hello world"))

        // Verify response contains responses from both platforms
        assertTrue(responses.containsKey(PlatformType.MINECRAFT))
        assertTrue(responses.containsKey(PlatformType.DISCORD))

        val minecraftResponses = responses[PlatformType.MINECRAFT]?.get("server-1")
        assertEquals(1, minecraftResponses?.size)
        assertEquals(TestResponse("p1: hello world"), minecraftResponses?.first())

        val discordResponses = responses[PlatformType.DISCORD]?.get("bot-1")
        assertEquals(1, discordResponses?.size)
        assertEquals(TestResponse("p2: hello world"), discordResponses?.first())
    }

    @Test
    fun `test query enriches message with metadata from enrichers`() = runTest {
        val enriched = client.query(TestData("hello query"))

        assertTrue(enriched.metadata.containsKey(PlatformType.MINECRAFT))
        val mcMetadata = enriched.metadata[PlatformType.MINECRAFT]?.get("server-1")
        assertEquals(1, mcMetadata?.size)
        assertEquals(TestMetadata("enriched: hello query"), mcMetadata?.first())
    }

    @Test
    fun `test dispatch with unhandled message data returns empty responses`() = runTest {
        val responses: PlatformMap<TestResponse> = client.dispatch(OtherData(42))

        assertTrue(responses.isEmpty())
    }
}
