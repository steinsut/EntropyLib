/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.network;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.dyn.renderer.entity.EntityDynRendererType;
import me.steinsut.entropylib.api.dyn.entity.IDynRenderedEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

public record ClientboundSetEntityDynRType(int id, EntityDynRendererType<?, ?, ?> dynType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundSetEntityDynRType> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "set_ent_dynr_t"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetEntityDynRType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ClientboundSetEntityDynRType::id,
            EntityDynRendererType.STREAM_CODEC,
            ClientboundSetEntityDynRType::dynType,
            ClientboundSetEntityDynRType::new
    );

    public static void handleOnMain(final ClientboundSetEntityDynRType data, final IPayloadContext context) {
        Level level = context.player().level();
        IDynRenderedEntity<?> entity = (IDynRenderedEntity<?>) level.getEntity(data.id);
        if (entity != null) {
            entity.setDynRendererType(data.dynType);
        }
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
