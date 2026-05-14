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
import me.steinsut.entropylib.api.dyn.renderer.entity.EntityDynRendererType;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class CommonRegistries {
    private static final int DYN_RENDERER_DATA_TYPE_MAX_ID = 65535;
    private static final int ENTITY_DYN_RENDERER_TYPE_MAX_ID = 65535;

    public static final ResourceKey<Registry<DynDataType<?>>> DYN_RENDERER_DATA_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(
            Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "dyn_data_types")
    );

    public static final ResourceKey<Registry<EntityDynRendererType<?, ?, ?>>> ENTITY_DYN_RENDERER_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(
            Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "dyn_type")
    );

    public static final Registry<DynDataType<?>> DYN_RENDERER_DATA_TYPE_REGISTRY = new RegistryBuilder<>(DYN_RENDERER_DATA_TYPE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "none"))
            .maxId(DYN_RENDERER_DATA_TYPE_MAX_ID)
            .create();

    public static final Registry<EntityDynRendererType<?, ?, ?>> ENTITY_DYN_RENDERER_TYPE_REGISTRY = new RegistryBuilder<>(ENTITY_DYN_RENDERER_TYPE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "none"))
            .maxId(ENTITY_DYN_RENDERER_TYPE_MAX_ID)
            .create();
}
