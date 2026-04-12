package me.steinsut.entropylib.api.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.steinsut.entropylib.api.dynrenderer.entity.IDynRenderedEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.NonNull;

public abstract class DynRenderedEntityRenderer<E extends Entity & IDynRenderedEntity<S>, S extends DynRenderedEntityRenderState<S>> extends EntityRenderer<E, S> {
    protected DynRenderedEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(@NonNull E entity, @NonNull S reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);

        reusedState.dynRenderer = entity.getDynRendererType().getDynRendererInstance().orElse(null);
        reusedState.dynRendererData = entity.getDynRendererDataHolder();
        reusedState.fallbackDynRenderer = entity.getFallbackDynRenderer();
    }

    @Override
    public void submit(@NonNull S state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);

        if (state.dynRenderer != null) {
            state.dynRenderer.useDataFrom(state.dynRendererData);
            state.dynRenderer.submit(state, poseStack, submitNodeCollector, camera);
        }
        else {
            state.fallbackDynRenderer.submit(state, poseStack, submitNodeCollector, camera);
        }
    }
}
