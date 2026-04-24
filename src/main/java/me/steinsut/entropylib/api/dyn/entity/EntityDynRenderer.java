package me.steinsut.entropylib.api.dyn.entity;

import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;
import me.steinsut.entropylib.api.dyn.renderer.BaseDynRenderer;
import me.steinsut.entropylib.api.dyn.data.DynDataType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public abstract class EntityDynRenderer<D, S extends DynRenderedEntityRenderState<S>> extends BaseDynRenderer<D, S> {
    public EntityDynRenderer(EntityRendererProvider.Context context, DynDataType<D> dataType) {
        super(dataType);
    }
}
