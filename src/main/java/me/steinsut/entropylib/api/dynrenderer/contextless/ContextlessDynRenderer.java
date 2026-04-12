package me.steinsut.entropylib.api.dynrenderer.contextless;

import me.steinsut.entropylib.api.dynrenderer.BaseDynRenderer;
import me.steinsut.entropylib.api.dynrenderer.DynDataType;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public abstract class ContextlessDynRenderer<D, S extends BaseRenderState> extends BaseDynRenderer<D, S> {
    public ContextlessDynRenderer(DynDataType<D, ?> dataType) {
        super(dataType);
    }
}
