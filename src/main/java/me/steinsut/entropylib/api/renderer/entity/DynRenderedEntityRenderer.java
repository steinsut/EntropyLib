package me.steinsut.entropylib.api.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.steinsut.entropylib.api.dynrenderer.entity.IDynRenderedEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;

public abstract class DynRenderedEntityRenderer<E extends Entity & IDynRenderedEntity<S>, S extends DynRenderedEntityRenderState<S>> extends EntityRenderer<E, S> {
    protected DynRenderedEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(E entity, S reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.dynRenderer = entity.getDynRendererType().getDynRendererInstance().orElse(null);
        reusedState.dynRendererData = entity.getDynRendererDataHolder();
    }

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);

        // TODO: state.dynRenderer can currently be null, should change that if possible
        if (state.dynRenderer != null) {
            state.dynRenderer.copyDataFrom(state.dynRendererData);
            state.dynRenderer.submit(state, poseStack, submitNodeCollector, camera);
        }
    }
}
