package me.steinsut.entropylib.api.entity;

import me.steinsut.entropylib.api.dynrenderer.DynDataType;
import me.steinsut.entropylib.api.dynrenderer.entity.EntityDynRendererType;
import me.steinsut.entropylib.api.dynrenderer.entity.IDynRenderedEntity;
import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;
import me.steinsut.entropylib.network.ClientboundSetEntityDynRType;
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

    public BaseDynRenderedEntity(EntityType<?> type, Level level, EntityDynRendererType<?, ?, S> dynRendererType) {
        super(type, level);

        this.setDynRendererType(dynRendererType);
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
            LOGGER.warn("DynRenderer dynType {} is incompatible with entity dynType {}",
                    DYN_RENDERER_TYPE_REGISTRY.getKey(type),
                    this.typeHolder().getKey().identifier());
        }
    }

    @Override
    public DynDataType.Holder<?> getDynDataHolder() {
        return this.dynRendererData;
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        this.dynRendererData.readData(input);
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        this.dynRendererData.writeData(output);
    }
}
