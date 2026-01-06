package xyz.rose.gateway.core

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Creates the Koin module for the core Gateway app.
 *
 * @param env The environment for the Gateway app
 * @return The gateway module
 */
fun gatewayModule(env: GatewayEnvironment) = module {
    single { env.logger }
    single { env.platforms }
    single { env.source }

    singleOf(env.configProvider)
    singleOf(env.appProvider)

    env.platforms.forEach { platform ->
        singleOf(platform.provider)
    }
}
