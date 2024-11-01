package gateway.conventions

tasks {
    register("setupDevEnv") {
        doFirst {
            System.setProperty("ENVIRONMENT", "dev")
        }
    }
}
