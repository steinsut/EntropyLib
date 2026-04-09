package me.steinsut.entropylib.api.renderer.dyn;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public abstract class BaseDynRenderer<D, S extends BaseRenderState> {
    private final DynRendererDataType<D, ?> dataType;
    private DynRendererDataType.Holder<D, ?> dataHolder;

    public BaseDynRenderer(DynRendererDataType<D, ?> dataType) {
        this.dataType = dataType;
        this.dataHolder = dataType.createHolder();
    }

    public void copyDataFrom(DynRendererDataType.Holder<?, ?> holder) { this.dataHolder.copyFrom(holder); }

    public DynRendererDataType.Holder<D, ?> getDataHolder() {
        return this.dataHolder;
    }

    public abstract void submit(S renderState, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState);
}
