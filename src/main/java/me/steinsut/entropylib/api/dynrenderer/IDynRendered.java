package me.steinsut.entropylib.api.dynrenderer;

import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public interface IDynRendered<S extends BaseRenderState> {
    DynDataType.Holder<?> getDynDataHolder();
}
