package me.steinsut.entropylib.api.registries;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.dynrenderer.BaseDynRendererType;
import me.steinsut.entropylib.api.dynrenderer.DynRendererDataType;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class Registries {
    private static final int DYN_RENDERER_DATA_TYPE_MAX_ID = 65535;
    private static final int DYN_RENDERER_TYPE_MAX_ID = 255;

    public static final ResourceKey<Registry<DynRendererDataType<?, ?>>> DYN_RENDERER_DATA_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(
            Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "dyn_data_types")
    );

    public static final ResourceKey<Registry<BaseDynRendererType<?, ?, ?>>> DYN_RENDERER_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(
            Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "dyn_type")
    );

    public static final Registry<DynRendererDataType<?, ?>> DYN_RENDERER_DATA_TYPE_REGISTRY = new RegistryBuilder<>(DYN_RENDERER_DATA_TYPE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "none"))
            .maxId(DYN_RENDERER_DATA_TYPE_MAX_ID)
            .create();

    public static final Registry<BaseDynRendererType<?, ?, ?>> DYN_RENDERER_TYPE_REGISTRY = new RegistryBuilder<>(DYN_RENDERER_TYPE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "none"))
            .maxId(DYN_RENDERER_TYPE_MAX_ID)
            .create();
}
