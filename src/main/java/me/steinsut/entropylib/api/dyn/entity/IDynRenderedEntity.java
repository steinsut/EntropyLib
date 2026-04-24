package me.steinsut.entropylib.api.dyn.entity;

import me.steinsut.entropylib.api.dyn.renderer.IDynRendered;
import me.steinsut.entropylib.api.dyn.contextless.ContextlessDynRenderer;
import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;

public interface IDynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends IDynRendered<S> {
    EntityDynRendererType<?, ?, S> getDynRendererType();
    ContextlessDynRenderer<?, S> getFallbackDynRenderer();

    void setDynRendererType(EntityDynRendererType<?, ?, ?> dynRendererType);
}
