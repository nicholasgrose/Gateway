package com.rose.gateway.minecraft.server

import com.rose.gateway.GatewayPlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Provides simple ways to schedule tasks to run on the Minecraft server
 */
object Scheduler : KoinComponent {
    private val plugin: GatewayPlugin by inject()

    /**
     * Schedules a task to be run on the next server tick
     *
     * @param task The task to schedule
     * @receiver The Bukkit server scheduler
     * @return The task being executed
     */
    fun runTask(task: () -> Unit): BukkitTask {
        return Bukkit.getScheduler().runTask(
            plugin,
            Runnable {
                task()
            }
        )
    }
}
