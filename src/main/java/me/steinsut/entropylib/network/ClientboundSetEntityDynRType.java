package me.steinsut.entropylib.network;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.dynrenderer.entity.EntityDynRendererType;
import me.steinsut.entropylib.api.dynrenderer.entity.IDynRenderedEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundSetEntityDynRType(int id, EntityDynRendererType<?, ?, ?> dynType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundSetEntityDynRType> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "cb_set_dynr_t"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetEntityDynRType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ClientboundSetEntityDynRType::id,
            EntityDynRendererType.STREAM_CODEC,
            ClientboundSetEntityDynRType::dynType,
            ClientboundSetEntityDynRType::new
    );

    public static void handleOnMain(final ClientboundSetEntityDynRType data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            IDynRenderedEntity<?> entity = (IDynRenderedEntity<?>) level.getEntity(data.id);
            if (entity != null) {
                entity.setDynRendererType(data.dynType);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
