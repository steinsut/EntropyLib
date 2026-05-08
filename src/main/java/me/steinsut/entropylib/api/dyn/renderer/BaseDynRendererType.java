/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.renderer;

import com.mojang.serialization.Codec;
import me.steinsut.entropylib.api.dyn.data.DynDataType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

import static me.steinsut.entropylib.api.registries.CommonRegistries.DYN_RENDERER_TYPE_REGISTRY;
import static me.steinsut.entropylib.api.registries.CommonRegistries.DYN_RENDERER_TYPE_REGISTRY_KEY;

public abstract class BaseDynRendererType<R extends BaseDynRenderer<D, S>, D, S extends BaseRenderState> {
    public static final Codec<BaseDynRendererType<?, ?, ?>> CODEC = DYN_RENDERER_TYPE_REGISTRY.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, BaseDynRendererType<?, ?, ?>> STREAM_CODEC = ByteBufCodecs.registry(DYN_RENDERER_TYPE_REGISTRY_KEY);

    protected final DynDataType<D> dataType;

    protected BaseDynRendererType(DynDataType<D> dataType) {
        this.dataType = dataType;
    }

    public final DynDataType<D> getDataType() {
        return this.dataType;
    }
}
