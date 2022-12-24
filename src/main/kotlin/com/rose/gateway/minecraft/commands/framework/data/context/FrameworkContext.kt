package com.rose.gateway.minecraft.commands.framework.data.context

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs

public sealed class FrameworkContext<A : CommandArgs<A>>(
    val command: Command,
    val args: A
) {
    public class BukkitCommand<B : BukkitContext, A : CommandArgs<A>>(
        val bukkit: B,
        command: Command,
        args: A
    ) : FrameworkContext<A>(command, args)
}

public typealias CommandExecuteContext<A> = FrameworkContext.BukkitCommand<BukkitContext.CommandExecute, A>
public typealias TabCompleteContext<A> = FrameworkContext.BukkitCommand<BukkitContext.TabComplete, A>
