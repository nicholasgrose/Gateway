package xyz.rose.gateway.core.core


interface GatewayBuilder {
    fun build(): GatewayApp
}

//fun main() {
//    GatewayBuilder()
//        .addConfig("dir/file.yaml")
//        .addPlatform(PaperProvider())
//    .build()
//    .start()
//}
