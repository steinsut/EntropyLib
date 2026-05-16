package me.steinsut.entropylib.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class EntropyLibCommands {
    private static final LiteralArgumentBuilder<CommandSourceStack> COMMAND_ROOT =
            Commands.literal("entropy")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        DynCommands.register(COMMAND_ROOT, buildContext);

        dispatcher.register(COMMAND_ROOT);
    }
}
