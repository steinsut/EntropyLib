package me.steinsut.entropylib.api.renderer.dyn.entity;

import me.steinsut.entropylib.api.renderer.DynRenderedEntityRenderState;
import me.steinsut.entropylib.api.renderer.dyn.IDynRendered;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Holder;

public non-sealed interface IDynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends IDynRendered<S> {
    EntityDynRendererType<?, ?, S> getDynRendererType();

    void setDynRendererType(Holder<EntityDynRendererType<?, ?, S>> dynRendererType);
}
