package me.steinsut.entropylib.api.dynrenderer.entity;

import me.steinsut.entropylib.api.dynrenderer.contextless.ContextlessDynRenderer;
import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;
import me.steinsut.entropylib.api.dynrenderer.IDynRendered;
import net.minecraft.core.Holder;

public non-sealed interface IDynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends IDynRendered<S> {
    EntityDynRendererType<?, ?, S> getDynRendererType();
    ContextlessDynRenderer<?, S> getFallbackDynRenderer();

    void setDynRendererType(Holder<EntityDynRendererType<?, ?, S>> dynRendererType);
}
