package me.steinsut.entropylib.api.dyn.renderer;

import me.steinsut.entropylib.api.dyn.data.DynDataType;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public interface IDynRendered<S extends BaseRenderState> {
    DynDataType.Holder<?> getDynDataHolder();
}
