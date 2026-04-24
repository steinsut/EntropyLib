package me.steinsut.entropylib.api.dyn.contextless;

import me.steinsut.entropylib.api.dyn.renderer.BaseDynRenderer;
import me.steinsut.entropylib.api.dyn.data.DynDataType;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public abstract class ContextlessDynRenderer<D, S extends BaseRenderState> extends BaseDynRenderer<D, S> {
    public ContextlessDynRenderer(DynDataType<D> dataType) {
        super(dataType);
    }
}
