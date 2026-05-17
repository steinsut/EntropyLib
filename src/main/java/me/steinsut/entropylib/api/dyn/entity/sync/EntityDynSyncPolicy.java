/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.entity.sync;

import com.mojang.serialization.Codec;

import java.util.function.Supplier;

import static me.steinsut.entropylib.api.registries.CommonRegistries.ENTITY_DYN_SYNC_POLICY_REGISTRY;

public class EntityDynSyncPolicy {
    public static Codec<EntityDynSyncPolicy> CODEC = ENTITY_DYN_SYNC_POLICY_REGISTRY.byNameCodec();

    private final Supplier<IEntityDynSyncHandler> handlerSupplier;

    public EntityDynSyncPolicy(Supplier<IEntityDynSyncHandler> handlerSupplier) {
        this.handlerSupplier = handlerSupplier;
    }

    public IEntityDynSyncHandler create() {
        return this.handlerSupplier.get();
    }
}
