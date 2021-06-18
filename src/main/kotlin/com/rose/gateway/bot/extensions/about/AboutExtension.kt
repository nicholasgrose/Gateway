package com.rose.gateway.bot.extensions.about

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.message.MessageLifetime.respondWithLifetime

class AboutExtension : Extension() {
    override val name = "version"

    override suspend fun setup() {
        command {
            name = "version"
            aliases = arrayOf("ver", "v")
            description = "Gives the current version of the Gateway plugin."

            action {
                Logger.log("${user?.username} requested plugin version!")
                message.respondWithLifetime {
                    content =
                        "I am currently version ${GatewayPlugin.VERSION}. All versions are available at https://github.com/nicholasgrose/Gateway/."
                }
            }
        }

        command {
            name = "blockgod"
            description = "Summon the block god for a moment."
            hidden = true

            action {
                Logger.log("${user?.username} knows the super secret command!")
                message.delete()
                user?.getDmChannelOrNull()?.createMessage("http://www.scpwiki.com/church-of-the-broken-god-hub")
            }
        }
    }
}
