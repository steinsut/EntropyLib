package me.steinsut.entropylib.api.renderer;

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

    public void setData(DynRendererDataType.Holder<?, ?> holder) {
        this.dataHolder.copyFrom(holder);
    }

    public D getData() {
        return this.dataHolder.getData();
    }

    public abstract void submit(S renderState, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState);
}
