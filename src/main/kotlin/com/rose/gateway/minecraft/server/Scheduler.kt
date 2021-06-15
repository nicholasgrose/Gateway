package com.rose.gateway.minecraft.server

import com.rose.gateway.GatewayPlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

object Scheduler {
    fun runTask(task: () -> Unit): BukkitTask {
        return Bukkit.getScheduler().runTask(GatewayPlugin.plugin, Runnable {
            task()
        })
    }
}
