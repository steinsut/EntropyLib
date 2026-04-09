package me.steinsut.entropylib.api.entity;

import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;
import me.steinsut.entropylib.api.dynrenderer.DynRendererDataType;
import me.steinsut.entropylib.api.dynrenderer.entity.EntityDynRendererType;
import me.steinsut.entropylib.api.dynrenderer.entity.IDynRenderedEntity;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import static me.steinsut.entropylib.EntropyLib.LOGGER;
import static me.steinsut.entropylib.api.registries.Registries.DYN_RENDERER_TYPE_REGISTRY;

public abstract class DynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends Entity implements IDynRenderedEntity<S> {
    protected EntityDynRendererType<?, ?, S> dynRendererType;
    protected DynRendererDataType.Holder<?, ?> dynRendererData;

    public DynRenderedEntity(EntityType<?> type, Level level, Holder<EntityDynRendererType<?, ?, S>> dynRendererType) {
        super(type, level);

        this.setDynRendererType(dynRendererType);
    }

    @Override
    public EntityDynRendererType<?, ?, S> getDynRendererType() {
        return this.dynRendererType;
    }

    public void setDynRenderer(EntityDynRendererType<?, ?, S> type) {
        if (type.isCompatible(this.typeHolder())) {
            this.dynRendererType = type;
            this.dynRendererData = type.getDataType().createHolder();
        } else {
            LOGGER.error("DynRenderer type {} is incompatible with entity type {}",
                    DYN_RENDERER_TYPE_REGISTRY.getKey(type),
                    this.typeHolder().getKey().identifier());
            if (this.dynRendererType == null) {
                //TODO: set type to some default here
            }
        }
    }

    @Override
    public DynRendererDataType.Holder<?, ?> getDynRendererDataHolder() {
        return this.dynRendererData;
    }
}
