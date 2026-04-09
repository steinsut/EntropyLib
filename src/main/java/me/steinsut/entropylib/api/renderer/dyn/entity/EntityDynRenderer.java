package me.steinsut.entropylib.api.renderer.dyn.entity;

import me.steinsut.entropylib.api.renderer.DynRenderedEntityRenderState;
import me.steinsut.entropylib.api.renderer.dyn.BaseDynRenderer;
import me.steinsut.entropylib.api.renderer.dyn.DynRendererDataType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public abstract class EntityDynRenderer<D, S extends DynRenderedEntityRenderState<S>> extends BaseDynRenderer<D, S> {
    public EntityDynRenderer(EntityRendererProvider.Context context, DynRendererDataType<D, ?> dataType) {
        super(dataType);
    }
}
