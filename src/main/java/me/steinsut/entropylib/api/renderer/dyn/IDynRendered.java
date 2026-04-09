package me.steinsut.entropylib.api.renderer.dyn;

import me.steinsut.entropylib.api.renderer.dyn.entity.IDynRenderedEntity;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public sealed interface IDynRendered<S extends BaseRenderState> permits IDynRenderedEntity {
    DynRendererDataType.Holder<?, ?> getDynRendererDataHolder();

    boolean isDynRendererInvalidated();
    void resetDynRendererInvalidation();
}
