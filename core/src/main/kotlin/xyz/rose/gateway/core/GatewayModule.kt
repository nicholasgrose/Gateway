package xyz.rose.gateway.core

import org.koin.core.module.dsl.onClose
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.onClose
import kotlin.reflect.KClass

/**
 * Creates the Koin module for the core Gateway app.
 *
 * @param env The environment for the Gateway app
 * @return The gateway module
 */
fun gateway(env: GatewayEnvironment) = module {
    val keyedSchemas = env.providers.map { it.getConfigSchema() }.map { it.key to it }
    val config = env.configBuilder.apply {
        schemas.addAll(keyedSchemas.map { it.second })
    }.build()

    includes(env.providers.map { it.createRuntimeModule(config) })

    single { config }
    keyedSchemas.forEach {
        val key = it.first

        // The suppressed cast is safe because the injectable type is compiled to always be identical to the schema's
        // serializer type.
        // Binding the config to its injectable type ensures that it can be injected into other components more easily.
        @Suppress("UNCHECKED_CAST")
        single { config.getConfig<Any>(key) } bind (it.second.injectableType as KClass<Any>)
    }

    single { params -> env.appProvider(this@single, params) } onClose { it?.stop() } bind GatewayApp::class
}
