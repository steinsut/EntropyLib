package me.steinsut.entropylib.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import me.steinsut.entropylib.api.dyn.entity.EntityDynType;
import me.steinsut.entropylib.api.dyn.entity.IDynEntity;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncPolicy;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

import static me.steinsut.entropylib.api.registries.CommonRegistries.DYN_ENTITY_SYNC_POLICY_REGISTRY_KEY;
import static me.steinsut.entropylib.api.registries.CommonRegistries.ENTITY_DYN_RENDERER_TYPE_REGISTRY_KEY;

public class DynEntityCommands {
    private static final DynamicCommandExceptionType ERROR_INVALID_ENTITY = new DynamicCommandExceptionType(
            (e) -> Component.translatable("commands.dyn.entity.invalid", e)
    );
    private static final Dynamic2CommandExceptionType ERROR_INCOMPATIBLE_DYN_RENDERER_TYPE = new Dynamic2CommandExceptionType(
            (t, et) -> Component.translatable("commands.dyn.entity.type.error.incompatible", t, et)
    );
    private static final DynamicCommandExceptionType ERROR_MISSING_DYN_RENDERER_TYPE = new DynamicCommandExceptionType(
            (t) -> Component.translatable("commands.dyn.entity.type.error.missing", t)
    );
    private static final DynamicCommandExceptionType ERROR_MISSING_DYN_SYNC_POLICY = new DynamicCommandExceptionType(
            (t) -> Component.translatable("commands.dyn.entity.sync.policy.error.missing", t)
    );
    private static final DynamicCommandExceptionType ERROR_DYN_DATA_READ = new DynamicCommandExceptionType(
            (m) -> Component.translatable("commands.dyn.entity.data.error.read", m)
    );
    private static final DynamicCommandExceptionType ERROR_DYN_DATA_WRITE = new DynamicCommandExceptionType(
            (m) -> Component.translatable("commands.dyn.entity.data.error.write", m)
    );
    private static final Dynamic2CommandExceptionType ERROR_DYN_DATA_PRESET_NOT_FOUND = new Dynamic2CommandExceptionType(
            (p, dt) -> Component.translatable("commands.dyn.entity.data.error.no_preset", p, dt)
    );
    private static final DynamicCommandExceptionType ERROR_DYN_SYNC_CONFIG_READ = new DynamicCommandExceptionType(
            (m) -> Component.translatable("commands.dyn.entity.sync.config.error.read", m)
    );

    private static void registerTypeCommands(ArgumentBuilder<CommandSourceStack, ?> parent, CommandBuildContext buildContext) {
        parent.then(
                Commands
                        .literal("type")
                        .then(
                                Commands
                                        .literal("get")
                                        .executes((context ->
                                                        runOnDynEntity(
                                                                EntityArgument.getEntity(context, "entity"),
                                                                (e, d) -> typeGet(context.getSource(), e, d)
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
                                                                        runOnDynEntity(
                                                                                EntityArgument.getEntity(context, "entity"),
                                                                                (e, d) -> typeSet(context.getSource(),
                                                                                        e, d,
                                                                                        ResourceArgument.getResource(context, "type", ENTITY_DYN_RENDERER_TYPE_REGISTRY_KEY))
                                                                        )

                                                                )
                                                        )
                                        )
                        )
        );
    }

    private static void registerDataCommands(ArgumentBuilder<CommandSourceStack, ?> parent, CommandBuildContext buildContext) {
        parent
                .then(
                        Commands
                                .literal("data")
                                .then(
                                        Commands
                                                .literal("get")
                                                .executes((context ->
                                                                runOnDynEntity(
                                                                        EntityArgument.getEntity(context, "entity"),
                                                                        (e, d) -> dataGet(context.getSource(), e, d)
                                                                )
                                                        )
                                                )
                                )
                                .then(
                                        Commands
                                                .literal("set")
                                                .then(
                                                        Commands
                                                                .literal("nbt")
                                                                .then(Commands
                                                                        .argument("nbt", CompoundTagArgument.compoundTag())
                                                                        .executes((context ->
                                                                                        runOnDynEntity(
                                                                                                EntityArgument.getEntity(context, "entity"),
                                                                                                (e, d) -> dataSet(
                                                                                                        context.getSource(),
                                                                                                        e, d, CompoundTagArgument.getCompoundTag(context, "nbt")
                                                                                                )
                                                                                        )

                                                                                )
                                                                        ))
                                                )
                                                .then(Commands
                                                        .literal("preset")
                                                        .then(Commands
                                                                .argument("preset", IdentifierArgument.id())
                                                                .executes((context -> runOnDynEntity(
                                                                        EntityArgument.getEntity(context, "entity"),
                                                                        (e, d) -> dataSetPreset(
                                                                                context.getSource(),
                                                                                e, d, IdentifierArgument.getId(context, "preset")
                                                                        )
                                                                )))))
                                )
                );
    }

    private static void registerSyncPolicyCommands(RequiredArgumentBuilder<CommandSourceStack, EntitySelector> parent, CommandBuildContext buildContext) {
        parent.then(
                Commands
                        .literal("sync")
                        .then(
                                Commands.literal("policy")
                                        .then(
                                                Commands
                                                        .literal("get")
                                                        .executes((context ->
                                                                        runOnDynEntity(
                                                                                EntityArgument.getEntity(context, "entity"),
                                                                                (e, d) -> syncPolicyGet(context.getSource(), e, d)
                                                                        )

                                                                )
                                                        )
                                        )
                                        .then(
                                                Commands
                                                        .literal("set")
                                                        .then(
                                                                Commands
                                                                        .argument("policy", ResourceArgument.resource(buildContext, DYN_ENTITY_SYNC_POLICY_REGISTRY_KEY))
                                                                        .executes((context ->
                                                                                        runOnDynEntity(
                                                                                                EntityArgument.getEntity(context, "entity"),
                                                                                                (e, d) -> syncPolicySet(context.getSource(),
                                                                                                        e, d,
                                                                                                        ResourceArgument.getResource(context, "policy", DYN_ENTITY_SYNC_POLICY_REGISTRY_KEY))
                                                                                        )

                                                                                )
                                                                        )
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("config")
                                        .then(
                                                Commands.literal("get")
                                                        .executes((context ->
                                                                runOnDynEntity(EntityArgument.getEntity(context, "entity"),
                                                                        (e, d) -> syncConfigGet(context.getSource(), e, d))))
                                        )
                        )

        );
    }

    private static int runOnDynEntity(Entity entity, ThrowingBiFunction<Entity, IDynEntity<?>> function) throws CommandSyntaxException {
        if (entity instanceof IDynEntity<?> dynEntity) {
            return function.run(entity, dynEntity);
        } else {
            throw ERROR_INVALID_ENTITY.create(entity.getDisplayName());
        }
    }

    private static int typeGet(CommandSourceStack source, Entity entity, IDynEntity<?> dynEntity) throws CommandSyntaxException {
        var dynType = dynEntity.getDynType();
        if (dynType == null) {
            source.sendSuccess(() -> Component.translatable("commands.dyn.entity.type.get.fallback", entity.getDisplayName()), false);
            return Command.SINGLE_SUCCESS;
        }

        var id = source.registryAccess().lookupOrThrow(ENTITY_DYN_RENDERER_TYPE_REGISTRY_KEY).getKey(dynType);
        if (id == null) {
            throw ERROR_MISSING_DYN_RENDERER_TYPE.create(dynType.toString());
        }

        source.sendSuccess(() -> Component.translatable("commands.dyn.entity.type.get", entity.getDisplayName(), id.toString()), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int typeSet(CommandSourceStack source, Entity entity, IDynEntity<?> dynEntity, Holder<EntityDynType<?, ?, ?>> typeHolder) throws CommandSyntaxException {
        var entityTypeHolder = entity.typeHolder();
        var dynType = typeHolder.value();
        if (!dynType.isCompatible(entityTypeHolder)) {
            throw ERROR_INCOMPATIBLE_DYN_RENDERER_TYPE.create(typeHolder.getRegisteredName(), entityTypeHolder.getRegisteredName());
        }

        dynEntity.setDynType(typeHolder.value());
        source.sendSuccess(() -> Component.translatable("commands.dyn.entity.type.set", entity.getDisplayName()), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int dataGet(CommandSourceStack source, Entity entity, IDynEntity<?> dynEntity) throws CommandSyntaxException {
        ProblemReporter.Collector collector = new ProblemReporter.Collector();
        var dataWriter = dynEntity.getDynDataWriter();

        var tagOutput = TagValueOutput.createWithoutContext(collector);
        dataWriter.storeData(tagOutput, "d");

        var tag = tagOutput.buildResult().getCompoundOrEmpty("d");
        if (!collector.isEmpty()) {
            throw ERROR_DYN_DATA_READ.create(collector.getReport());
        }

        source.sendSuccess(() -> Component.translatable("commands.dyn.entity.data.get", entity.getDisplayName(), NbtUtils.prettyPrint(tag, false)), false);
        return Command.SINGLE_SUCCESS;
    }


    private static int dataSetPreset(CommandSourceStack source, Entity entity, IDynEntity<?> dynEntity, Identifier presetId) throws CommandSyntaxException {
        if (!dynEntity.getDynType().getDataType().hasPreset(presetId)) {
            var dynRendererType = dynEntity.getDynType();
            var dynRendererTypeId = source.registryAccess().lookupOrThrow(ENTITY_DYN_RENDERER_TYPE_REGISTRY_KEY).getKey(dynEntity.getDynType());
            if (dynRendererTypeId == null) {
                throw ERROR_MISSING_DYN_RENDERER_TYPE.create(dynRendererType);
            }

            throw ERROR_DYN_DATA_PRESET_NOT_FOUND.create(dynRendererTypeId.toString(), presetId.toString());
        }

        dynEntity.setDynDataToPreset(presetId, true);
        source.sendSuccess(() -> Component.translatable("commands.dyn.entity.data.set", entity.getDisplayName()), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int dataSet(CommandSourceStack source, Entity entity, IDynEntity<?> dynEntity, CompoundTag tag) throws CommandSyntaxException {
        ProblemReporter.Collector collector = new ProblemReporter.Collector();
        CompoundTag dataTag = new CompoundTag();

        dataTag.put("d", tag);
        var holder = dynEntity.getDynType().getDataType().createHolder();
        var tagInput = TagValueInput.create(collector, source.registryAccess(), dataTag);

        holder.getReader().readData(tagInput, "d");
        if (!collector.isEmpty()) {
            throw ERROR_DYN_DATA_WRITE.create(collector.getReport());
        }

        dynEntity.readDynDataFrom(holder.getWriter(), true);
        source.sendSuccess(() -> Component.translatable("commands.dyn.entity.data.set", entity.getDisplayName()), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int syncPolicyGet(CommandSourceStack source, Entity entity, IDynEntity<?> dynEntity) throws CommandSyntaxException {
        var policy = dynEntity.getDynSyncPolicy();
        var id = source.registryAccess().lookupOrThrow(DYN_ENTITY_SYNC_POLICY_REGISTRY_KEY).getKey(policy);
        if (id == null) {
            throw ERROR_MISSING_DYN_SYNC_POLICY.create(policy);
        }

        source.sendSuccess(() -> Component.translatable("commands.dyn.entity.sync.policy.get", entity.getDisplayName(), id.toString()), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int syncPolicySet(CommandSourceStack source, Entity entity, IDynEntity<?> dynEntity, Holder<DynEntitySyncPolicy> policyHolder) {
        dynEntity.setDynSyncPolicy(policyHolder.value());
        source.sendSuccess(() -> Component.translatable("commands.dyn.entity.sync.policy.set", entity.getDisplayName()), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int syncConfigGet(CommandSourceStack source, Entity entity, IDynEntity<?> dynEntity) throws CommandSyntaxException {
        ProblemReporter.Collector collector = new ProblemReporter.Collector();
        var configurator = dynEntity.getDynSyncConfigurator();

        var tagOutput = TagValueOutput.createWithoutContext(collector);
        configurator.writeConfiguration(tagOutput);

        var tag = tagOutput.buildResult();
        if (!collector.isEmpty()) {
            throw ERROR_DYN_SYNC_CONFIG_READ.create(collector.getReport());
        }

        source.sendSuccess(() -> Component.translatable("commands.dyn.entity.sync.config.get", entity.getDisplayName(), NbtUtils.prettyPrint(tag, false)), false);
        return Command.SINGLE_SUCCESS;
    }

    public static void register(ArgumentBuilder<CommandSourceStack, ?> parent, CommandBuildContext buildContext) {
        var entityNode = Commands.literal("entity");
        var argumentNode = Commands.argument("entity", EntityArgument.entity());

        registerTypeCommands(argumentNode, buildContext);
        registerDataCommands(argumentNode, buildContext);
        registerSyncPolicyCommands(argumentNode, buildContext);

        entityNode.then(argumentNode);
        parent.then(entityNode);
    }


    @FunctionalInterface
    private interface ThrowingBiFunction<T, U> {
        int run(T t, U u) throws CommandSyntaxException;
    }
}
