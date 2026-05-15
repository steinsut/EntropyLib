package me.steinsut.entropylib.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import me.steinsut.entropylib.api.dyn.entity.IDynRenderedEntity;
import me.steinsut.entropylib.api.dyn.renderer.entity.EntityDynRendererType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import static me.steinsut.entropylib.api.registries.CommonRegistries.ENTITY_DYN_RENDERER_TYPE_REGISTRY_KEY;

public class DynEntityCommands {
    private static final DynamicCommandExceptionType ERROR_INVALID_ENTITY = new DynamicCommandExceptionType(
            (e) -> Component.translatable("commands.dyn.entity.invalid", e)
    );
    private static final Dynamic2CommandExceptionType ERROR_INCOMPATIBLE_DYN_RENDERER_TYPE = new Dynamic2CommandExceptionType(
            (t, e) -> Component.translatable("commands.dyn.entity.type.set.incompatible", t, e)
    );
    private static final DynamicCommandExceptionType ERROR_MISSING_DYN_RENDERER_TYPE = new DynamicCommandExceptionType(
            (t) -> Component.translatable("commands.dyn.entity.type.missing", t)
    );

    private static void registerTypeCommands(ArgumentBuilder<CommandSourceStack, ?> parent, CommandBuildContext buildContext) {
        parent.then(
                Commands
                        .literal("type")
                        .then(
                                Commands
                                        .literal("get")
                                        .executes((context ->
                                                        executeTypeGet(
                                                                context.getSource(),
                                                                EntityArgument.getEntity(context, "entity")
                                                        )
                                                )
                                        )
                        )
                        .then(
                                Commands
                                        .literal("set")
                                        .then(
                                                Commands
                                                        .argument("type", ResourceArgument.resource(buildContext, ENTITY_DYN_RENDERER_TYPE_REGISTRY_KEY))
                                                        .executes((context ->
                                                                        executeTypeSet(context.getSource(),
                                                                                EntityArgument.getEntity(context, "entity"),
                                                                                ResourceArgument.getResource(context, "type", ENTITY_DYN_RENDERER_TYPE_REGISTRY_KEY))
                                                                )
                                                        )
                                        )
                        )
        );
    }

    private static void registerDataCommands(ArgumentBuilder<CommandSourceStack, ?> parent, CommandBuildContext buildContext) {

    }

    private static int executeTypeSet(CommandSourceStack source, Entity entity, Holder<EntityDynRendererType<?, ?, ?>> typeHolder) throws CommandSyntaxException {
        var entityTypeHolder = entity.typeHolder();
        if (entity instanceof IDynRenderedEntity<?> dynEntity) {
            var dynType = typeHolder.value();
            if (!dynType.isCompatible(entityTypeHolder)) {
                throw ERROR_INCOMPATIBLE_DYN_RENDERER_TYPE.create(typeHolder.getRegisteredName(), entityTypeHolder.getRegisteredName());
            }

            dynEntity.setDynRendererType(typeHolder.value());
            source.sendSuccess(() -> Component.translatable("commands.dyn.entity.type.set.success", entity.getName(), typeHolder.getRegisteredName()), false);
            return 1;
        } else {
            throw ERROR_INVALID_ENTITY.create(entity.getName());
        }
    }

    private static int executeTypeGet(CommandSourceStack source, Entity entity) throws CommandSyntaxException {
        if (entity instanceof IDynRenderedEntity<?> e) {
            var dynRendererType = e.getDynRendererType();
            if (dynRendererType == null) {
                source.sendSuccess(() -> Component.translatable("commands.dyn.entity.type.get", entity.getName(), "null"), false);
                return 1;
            }

            var id = source.registryAccess().lookupOrThrow(ENTITY_DYN_RENDERER_TYPE_REGISTRY_KEY).getKey(dynRendererType);
            if (id == null) {
                throw ERROR_MISSING_DYN_RENDERER_TYPE.create(dynRendererType.toString());
            }

            source.sendSuccess(() -> Component.translatable("commands.dyn.entity.type.get", entity.getName(), id.toString()), false);
            return 1;
        } else {
            throw ERROR_INVALID_ENTITY.create(entity.getName());
        }
    }

    public static void register(ArgumentBuilder<CommandSourceStack, ?> parent, CommandBuildContext buildContext) {
        var entityNode = Commands.literal("entity");
        var argumentNode = Commands.argument("entity", EntityArgument.entity());

        registerTypeCommands(argumentNode, buildContext);
        registerDataCommands(argumentNode, buildContext);

        entityNode.then(argumentNode);
        parent.then(entityNode);
    }
}
