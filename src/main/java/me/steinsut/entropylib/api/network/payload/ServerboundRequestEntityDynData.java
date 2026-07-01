package me.steinsut.entropylib.api.network.payload;

import io.netty.buffer.ByteBuf;
import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.dyn.data.DynDataType;
import me.steinsut.entropylib.api.dyn.entity.DynEntity;
import me.steinsut.entropylib.network.payload.ClientboundUpdateEntityDynData;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundRequestEntityDynData(int entityId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundRequestEntityDynData> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "req_ent_dyndata"));

    public static StreamCodec<ByteBuf, ServerboundRequestEntityDynData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ServerboundRequestEntityDynData::entityId,
            ServerboundRequestEntityDynData::new
    );

    public static void handleOnMain(final ServerboundRequestEntityDynData payload, final IPayloadContext context)  {
        ServerPlayer player = (ServerPlayer) context.player();
        Entity entity = player.level().getEntity(payload.entityId);

        if (entity instanceof DynEntity<?> dynEntity) {
            PacketDistributor.sendToPlayer(
                    player,
                    new ClientboundUpdateEntityDynData(payload.entityId, dynEntity.getDataWriter().makeHolderClone())
            );
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
