package me.steinsut.entropylib.api.entity;

import me.steinsut.entropylib.api.dyn.data.DynDataType;
import me.steinsut.entropylib.api.dyn.renderer.BaseDynRendererType;
import me.steinsut.entropylib.api.dyn.renderer.entity.EntityDynRendererType;
import me.steinsut.entropylib.api.dyn.renderer.entity.IDynRenderedEntity;
import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;
import me.steinsut.entropylib.network.ClientboundSetEntityDynRType;
import me.steinsut.entropylib.network.sync.EntityDynSyncPolicy;
import me.steinsut.entropylib.network.sync.IEntityDynSyncHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;

import static me.steinsut.entropylib.EntropyLib.LOGGER;
import static me.steinsut.entropylib.api.registries.CommonRegistries.DYN_RENDERER_TYPE_REGISTRY;

public abstract class BaseDynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends Entity implements IDynRenderedEntity<S> {
    protected EntityDynRendererType<?, ?, S> dynRendererType;
    protected DynDataType.Holder<?> dynRendererData;
    protected EntityDynSyncPolicy dynSyncPolicy;
    protected IEntityDynSyncHandler dynSyncHandler;

    public BaseDynRenderedEntity(EntityType<?> type, Level level, EntityDynRendererType<?, ?, S> dynRendererType, EntityDynSyncPolicy dynSyncPolicy) {
        super(type, level);

        this.setDynRendererType(dynRendererType);
        this.setDynSyncPolicy(dynSyncPolicy);
    }

    @Override
    public EntityDynRendererType<?, ?, S> getDynRendererType() {
        return this.dynRendererType;
    }

    @Override
    public void setDynRendererType(EntityDynRendererType<?, ?, ?> type) {
        if (type.isCompatible(this.typeHolder())) {
            //noinspection unchecked
            this.dynRendererType = (EntityDynRendererType<?, ?, S>) type;
            this.dynRendererData = type.getDataType().createHolder();

            if (!this.level().isClientSide()) {
                PacketDistributor.sendToPlayersTrackingChunk(
                        (ServerLevel) this.level(),
                        this.chunkPosition(),
                        new ClientboundSetEntityDynRType(this.getId(), this.dynRendererType)
                );
            }
        } else {
            LOGGER.warn("DynRendererType {} is incompatible with entity type {}",
                    DYN_RENDERER_TYPE_REGISTRY.getKey(type),
                    this.typeHolder().getKey().identifier());
        }
    }

    @Override
    public void setDynSyncPolicy(EntityDynSyncPolicy policy) {
        this.dynSyncPolicy = policy;
        this.dynSyncHandler = policy.create();
    }

    @Override
    public DynDataType.Holder<?> getDynDataHolder() {
        return this.dynRendererData;
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        input.child("dyn").ifPresent((c) -> {
            c.read("r_type", BaseDynRendererType.CODEC).ifPresent((t) -> {
                //noinspection unchecked
                this.dynRendererType = (EntityDynRendererType<?, ?, S>) t;
                this.dynRendererData = this.dynRendererType.getDataType().createHolder();

                this.dynRendererData.readData(input);
            });

            if (this.level().isClientSide()) {
                return;
            }

            c.read("s_pol", EntityDynSyncPolicy.CODEC).ifPresent((p) -> {
                this.dynSyncPolicy = p;
                this.dynSyncHandler = p.create();

                c.child("s_data").ifPresent((d) -> {
                    this.dynSyncHandler.readConfiguration(d);
                });
            });
        });
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        ValueOutput dynChild = output.child("dyn");

        dynChild.store("r_type", BaseDynRendererType.CODEC, this.dynRendererType);
        this.dynRendererData.writeData(dynChild);

        if (this.level().isClientSide()) {
            return;
        }

        dynChild.store("s_pol", EntityDynSyncPolicy.CODEC, this.dynSyncPolicy);
        ValueOutput syncDataChild = dynChild.child("s_data");
        this.dynSyncHandler.writeConfiguration(syncDataChild);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            this.dynSyncHandler.onTick();
        }
    }
}
