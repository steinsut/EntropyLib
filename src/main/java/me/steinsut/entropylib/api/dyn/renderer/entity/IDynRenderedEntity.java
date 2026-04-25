package me.steinsut.entropylib.api.dyn.renderer.entity;

import me.steinsut.entropylib.api.dyn.renderer.IDynRendered;
import me.steinsut.entropylib.api.dyn.contextless.ContextlessDynRenderer;
import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;
import me.steinsut.entropylib.api.dyn.sync.entity.EntityDynSyncPolicy;

public interface IDynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends IDynRendered<S> {
    EntityDynRendererType<?, ?, S> getDynRendererType();
    ContextlessDynRenderer<?, S> getFallbackDynRenderer();

    void setDynRendererType(EntityDynRendererType<?, ?, ?> dynRendererType);
    void setDynSyncPolicy(EntityDynSyncPolicy policy);
}
