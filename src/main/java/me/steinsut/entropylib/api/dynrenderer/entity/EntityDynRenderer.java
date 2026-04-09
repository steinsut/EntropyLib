package me.steinsut.entropylib.api.dynrenderer.entity;

import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;
import me.steinsut.entropylib.api.dynrenderer.BaseDynRenderer;
import me.steinsut.entropylib.api.dynrenderer.DynRendererDataType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public abstract class EntityDynRenderer<D, S extends DynRenderedEntityRenderState<S>> extends BaseDynRenderer<D, S> {
    public EntityDynRenderer(EntityRendererProvider.Context context, DynRendererDataType<D, ?> dataType) {
        super(dataType);
    }
}
