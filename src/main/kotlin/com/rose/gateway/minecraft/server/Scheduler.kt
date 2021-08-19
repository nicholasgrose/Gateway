package com.rose.gateway.minecraft.server

import com.rose.gateway.GatewayPlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class Scheduler(val plugin: GatewayPlugin) {
    fun runTask(task: () -> Unit): BukkitTask {
        return Bukkit.getScheduler().runTask(plugin, Runnable {
            task()
        })
    }
}
