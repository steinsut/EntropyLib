package me.steinsut.entropylib.api.renderer;

import me.steinsut.entropylib.api.renderer.dyn.DynRendererDataType;
import me.steinsut.entropylib.api.renderer.dyn.entity.EntityDynRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class DynRenderedEntityRenderState<S extends DynRenderedEntityRenderState<S>> extends EntityRenderState {
    public EntityDynRenderer<?, S> dynRenderer;
    public DynRendererDataType.Holder<?, ?> dynRendererData;
}
