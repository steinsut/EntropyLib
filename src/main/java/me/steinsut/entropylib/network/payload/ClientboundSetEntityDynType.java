/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.network.payload;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.dyn.entity.EntityDynType;
import me.steinsut.entropylib.api.dyn.entity.DynEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

public record ClientboundSetEntityDynType(int entityId,
                                          EntityDynType<?, ?, ?> dynType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundSetEntityDynType> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "set_ent_dynr_t"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetEntityDynType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ClientboundSetEntityDynType::entityId,
            EntityDynType.STREAM_CODEC,
            ClientboundSetEntityDynType::dynType,
            ClientboundSetEntityDynType::new
    );

    public static void handleOnMain(final ClientboundSetEntityDynType data, final IPayloadContext context) {
        Level level = context.player().level();
        DynEntity<?> entity = (DynEntity<?>) level.getEntity(data.entityId);
        if (entity != null) {
            entity.setDynType(data.dynType);
        }
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
