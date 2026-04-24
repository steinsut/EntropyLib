package me.steinsut.entropylib.network;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.dyn.data.DynDataType;
import me.steinsut.entropylib.api.dyn.entity.IDynRenderedEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

public record ClientboundUpdateEntityDynRData(int id, DynDataType.Holder<?> holder) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundUpdateEntityDynRData> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "cb_set_ent_dyndata"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateEntityDynRData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ClientboundUpdateEntityDynRData::id,
            DynDataType.Holder.STREAM_CODEC,
            ClientboundUpdateEntityDynRData::holder,
            ClientboundUpdateEntityDynRData::new
    );

    public static void handleOnMain(final ClientboundUpdateEntityDynRData data, final IPayloadContext context) {
        Level level = context.player().level();
        IDynRenderedEntity<?> entity = (IDynRenderedEntity<?>) level.getEntity(data.id);
        if (entity != null) {
            entity.getDynDataHolder().copyFrom(data.holder);
        }
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
