/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.registries;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.dyn.data.DynDataType;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncPolicy;
import me.steinsut.entropylib.api.dyn.renderer.entity.DynEntityRendererType;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class CommonRegistries {
    public static final ResourceKey<Registry<DynDataType<?>>> DYN_DATA_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(
            Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "dyn_data_types")
    );
    public static final ResourceKey<Registry<DynEntityRendererType<?, ?, ?>>> DYN_ENTITY_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(
            Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "dyn_entity_types")
    );
    public static final ResourceKey<Registry<DynEntitySyncPolicy>> DYN_ENTITY_SYNC_POLICY_REGISTRY_KEY = ResourceKey.createRegistryKey(
            Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "sync_policies")
    );

    private static final int DYN_RENDERER_DATA_TYPE_MAX_ID = 65535;
    public static final Registry<DynDataType<?>> DYN_RENDERER_DATA_TYPE_REGISTRY = new RegistryBuilder<>(DYN_DATA_TYPE_REGISTRY_KEY)
            .sync(true)
            .maxId(DYN_RENDERER_DATA_TYPE_MAX_ID)
            .create();

    private static final int ENTITY_DYN_RENDERER_TYPE_MAX_ID = 65535;
    public static final Registry<DynEntityRendererType<?, ?, ?>> DYN_ENTITY_RENDERER_TYPE_REGISTRY = new RegistryBuilder<>(DYN_ENTITY_TYPE_REGISTRY_KEY)
            .sync(true)
            .maxId(ENTITY_DYN_RENDERER_TYPE_MAX_ID)
            .create();

    private static final int ENTITY_DYN_SYNC_POLICY_MAX_ID = 255;
    public static final Registry<DynEntitySyncPolicy> DYN_ENTITY_SYNC_POLICY_REGISTRY = new RegistryBuilder<>(DYN_ENTITY_SYNC_POLICY_REGISTRY_KEY)
            .sync(false)
            .defaultKey(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "none"))
            .maxId(ENTITY_DYN_SYNC_POLICY_MAX_ID)
            .create();
}
