package me.steinsut.entropylib.api.dynrenderer.entity;

import me.steinsut.entropylib.api.dynrenderer.IDynRendered;
import me.steinsut.entropylib.api.dynrenderer.contextless.ContextlessDynRenderer;
import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;

public interface IDynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends IDynRendered<S> {
    EntityDynRendererType<?, ?, S> getDynRendererType();
    ContextlessDynRenderer<?, S> getFallbackDynRenderer();

    void setDynRendererType(EntityDynRendererType<?, ?, ?> dynRendererType);
}
