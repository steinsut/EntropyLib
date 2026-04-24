package me.steinsut.entropylib.api.renderer.entity;

import me.steinsut.entropylib.api.dyn.data.DynDataType;
import me.steinsut.entropylib.api.dyn.contextless.ContextlessDynRenderer;
import me.steinsut.entropylib.api.dyn.renderer.entity.EntityDynRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class DynRenderedEntityRenderState<S extends DynRenderedEntityRenderState<S>> extends EntityRenderState {
    public EntityDynRenderer<?, S> dynRenderer;
    public DynDataType.Holder<?> dynRendererData;
    public ContextlessDynRenderer<?, S> fallbackDynRenderer;
}
