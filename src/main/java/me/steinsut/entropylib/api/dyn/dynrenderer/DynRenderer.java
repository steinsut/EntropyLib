/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.dynrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.steinsut.entropylib.api.dyn.data.DynDataType;
import me.steinsut.entropylib.api.dyn.data.DynDataWriter;
import me.steinsut.entropylib.util.Cloneable;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public abstract class DynRenderer<D extends Cloneable<D>, S extends BaseRenderState> {
    protected final DynDataType<D> dataType;
    protected final DynDataType.Holder<D> dataHolder;

    public DynRenderer(DynDataType<D> dataType) {
        this.dataType = dataType;
        this.dataHolder = dataType.createHolder();
    }

    public void readDataFrom(DynDataWriter<?> writer) {
        writer.writeToHolder(this.dataHolder);
    }

    public void setDataToPreset(Identifier id) {
        this.dataHolder.setToPreset(id);
    }

    public abstract void submit(S renderState, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState);
}
