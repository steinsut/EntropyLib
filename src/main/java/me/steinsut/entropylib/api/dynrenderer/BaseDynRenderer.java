package me.steinsut.entropylib.api.dynrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public abstract class BaseDynRenderer<D, S extends BaseRenderState> {
    private final DynDataType<D, ?> dataType;
    private final DynDataType.Holder<D, ?> dataHolder;

    public BaseDynRenderer(DynDataType<D, ?> dataType) {
        this.dataType = dataType;
        this.dataHolder = dataType.createHolder();
    }

    public void copyDataFrom(DynDataType.Holder<?, ?> holder) {
        this.dataHolder.copyFrom(holder);
    }

    public DynDataType.Holder<D, ?> getDataHolder() {
        return this.dataHolder;
    }

    public abstract void submit(S renderState, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState);
}
