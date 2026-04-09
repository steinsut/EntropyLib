package me.steinsut.entropylib.api.renderer.dyn.entity;

import me.steinsut.entropylib.api.renderer.dyn.IDynRendered;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public non-sealed interface IDynRenderedEntity<S extends EntityRenderState> extends IDynRendered<S> {
    EntityDynRendererType<?, ?, S> getDynRendererType();
    void setDynRendererType(EntityDynRendererType<?, ?, S> dynRendererType);
}
