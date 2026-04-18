package me.steinsut.entropylib.api.entity;

import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;
import me.steinsut.entropylib.api.dynrenderer.DynDataType;
import me.steinsut.entropylib.api.dynrenderer.entity.EntityDynRendererType;
import me.steinsut.entropylib.api.dynrenderer.entity.IDynRenderedEntity;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import static me.steinsut.entropylib.EntropyLib.LOGGER;
import static me.steinsut.entropylib.api.registries.Registries.DYN_RENDERER_TYPE_REGISTRY;

public abstract class BaseDynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends Entity implements IDynRenderedEntity<S> {
    protected EntityDynRendererType<?, ?, S> dynRendererType;
    protected DynDataType.Holder<?, ?> dynRendererData;

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
        } else {
            LOGGER.warn("DynRenderer dynType {} is incompatible with entity dynType {}",
                    DYN_RENDERER_TYPE_REGISTRY.getKey(type),
                    this.typeHolder().getKey().identifier());
        }
    }

    @Override
    public DynDataType.Holder<?, ?> getDynDataHolder() {
        return this.dynRendererData;
    }
}
