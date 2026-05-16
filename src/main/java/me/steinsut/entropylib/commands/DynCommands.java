package me.steinsut.entropylib.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

public class DynCommands {
    private static final LiteralArgumentBuilder<CommandSourceStack> DYN_COMMAND_ROOT =
            net.minecraft.commands.Commands.literal("dyn");

    public static void register(ArgumentBuilder<CommandSourceStack, ?> parent, CommandBuildContext buildContext) {
        DynEntityCommands.register(DYN_COMMAND_ROOT, buildContext);

        parent.then(DYN_COMMAND_ROOT);
    }
}
