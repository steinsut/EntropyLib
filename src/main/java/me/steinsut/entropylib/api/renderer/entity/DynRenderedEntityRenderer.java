/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.steinsut.entropylib.api.dyn.renderer.entity.IDynRenderedEntity;
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
        reusedState.dynRendererData = entity.getDynDataHolder();
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
