package me.steinsut.entropylib.api.network.payload;

import io.netty.buffer.ByteBuf;
import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.dyn.entity.DynEntity;
import me.steinsut.entropylib.network.payload.ClientboundSetEntityDynType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

public record ServerboundRequestEntityDynType(int entityId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerboundRequestEntityDynType> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "req_ent_dyntype"));

    public static StreamCodec<ByteBuf, ServerboundRequestEntityDynType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ServerboundRequestEntityDynType::entityId,
            ServerboundRequestEntityDynType::new
    );

    public static void handleOnMain(final ServerboundRequestEntityDynType payload, final IPayloadContext context)  {
        ServerPlayer player = (ServerPlayer) context.player();
        Entity entity = player.level().getEntity(payload.entityId);

        if (entity instanceof DynEntity<?> dynEntity) {
            PacketDistributor.sendToPlayer(player, new ClientboundSetEntityDynType(
                    payload.entityId,
                    dynEntity.getDynType()
            ));
        }
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
