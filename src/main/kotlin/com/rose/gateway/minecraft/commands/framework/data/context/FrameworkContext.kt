package com.rose.gateway.minecraft.commands.framework.data.context

import com.rose.gateway.minecraft.commands.framework.Command
import com.rose.gateway.minecraft.commands.framework.runner.ArgParser
import com.rose.gateway.minecraft.commands.framework.runner.CommandArgs

public sealed class FrameworkContext<A : CommandArgs<A>>(
    val command: Command,
    val args: A
) {
    public sealed class BukkitCommand<B : BukkitContext, A : CommandArgs<A>>(
        val bukkit: B,
        command: Command,
        args: A
    ) : FrameworkContext<A>(command, args) {
        public class CommandExecute<A : CommandArgs<A>>(
            bukkit: BukkitContext.CommandExecute,
            command: Command,
            args: A
        ) : BukkitCommand<BukkitContext.CommandExecute, A>(
            bukkit,
            command,
            args
        )

        public class TabComplete<A : CommandArgs<A>>(
            bukkit: BukkitContext.TabComplete,
            command: Command,
            args: A,
            val completingParser: ArgParser<*, A, *>
        ) : BukkitCommand<BukkitContext.TabComplete, A>(
            bukkit,
            command,
            args
        )
    }
}

public typealias CommandExecuteContext<A> = FrameworkContext.BukkitCommand.CommandExecute<A>
public typealias TabCompleteContext<A> = FrameworkContext.BukkitCommand.TabComplete<A>
