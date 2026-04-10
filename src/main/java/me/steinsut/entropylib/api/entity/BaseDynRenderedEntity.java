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

public abstract class BaseDynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends Entity implements IDynRenderedEntity<S> {
    protected EntityDynRendererType<?, ?, S> dynRendererType;
    protected DynDataType.Holder<?, ?> dynRendererData;

    public BaseDynRenderedEntity(EntityType<?> type, Level level, Holder<EntityDynRendererType<?, ?, S>> dynRendererType) {
        super(type, level);

        this.setDynRendererType(dynRendererType);
    }

    @Override
    public EntityDynRendererType<?, ?, S> getDynRendererType() {
        return this.dynRendererType;
    }

    @Override
    public void setDynRendererType(Holder<EntityDynRendererType<?, ?, S>> typeHolder) {
        var type = typeHolder.value();

        if (type.isCompatible(this.typeHolder())) {
            this.dynRendererType = type;
            this.dynRendererData = type.getDataType().createHolder();
        } else {
            LOGGER.warn("DynRenderer type {} is incompatible with entity type {}",
                    typeHolder.getKey().identifier(),
                    this.typeHolder().getKey().identifier());
            if (this.dynRendererType == null) {
                this.dynRendererType = this.getFallbackDynRendererType();
                this.dynRendererData = this.dynRendererType.getDataType().createHolder();

                if (this.level().isClientSide() && this.dynRendererType.getDynRendererInstance().isEmpty()) {
                    this.dynRendererType.instantiateDynRenderer(null);
                }

                this.setFallbackDynRendererData();
            }
        }
    }

    @Override
    public DynDataType.Holder<?, ?> getDynRendererDataHolder() {
        return this.dynRendererData;
    }
}
