package com.rose.gateway.minecraft.server

import com.rose.gateway.GatewayPlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object Scheduler : KoinComponent {
    private val plugin: GatewayPlugin by inject()

    fun runTask(task: () -> Unit): BukkitTask {
        return Bukkit.getScheduler().runTask(
            plugin,
            Runnable {
                task()
            }
        )
    }
}
