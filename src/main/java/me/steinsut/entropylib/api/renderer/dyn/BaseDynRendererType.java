package me.steinsut.entropylib.api.renderer.dyn;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

import static me.steinsut.entropylib.api.registries.Registries.DYN_RENDERER_TYPE_REGISTRY;
import static me.steinsut.entropylib.api.registries.Registries.DYN_RENDERER_TYPE_REGISTRY_KEY;

public abstract class BaseDynRendererType<R extends BaseDynRenderer<D, S>, D, S extends BaseRenderState> {
    public static final Codec<BaseDynRendererType<?, ?, ?>> CODEC = DYN_RENDERER_TYPE_REGISTRY.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, BaseDynRendererType<?, ?, ?>> STREAM_CODEC = ByteBufCodecs.registry(DYN_RENDERER_TYPE_REGISTRY_KEY);

    protected final DynRendererDataType<D, ?> dataType;

    public BaseDynRendererType(DynRendererDataType<D, ?> dataType) {
        this.dataType = dataType;
    }

    public final DynRendererDataType<D, ?> getDataType() {
        return this.dataType;
    }
}
