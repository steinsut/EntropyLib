package me.steinsut.entropylib.api.dynrenderer;

import me.steinsut.entropylib.api.dynrenderer.entity.IDynRenderedEntity;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public sealed interface IDynRendered<S extends BaseRenderState> permits IDynRenderedEntity {
    DynDataType.Holder<?, ?> getDynDataHolder();
}
