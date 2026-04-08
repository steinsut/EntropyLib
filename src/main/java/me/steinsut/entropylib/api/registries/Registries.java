package me.steinsut.entropylib.api.registries;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.model.ModelDataType;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class Registries {
    private static final int MODEL_DATA_TYPE_REGISTRY_MAX_ID = 65536;

    public static final ResourceKey<Registry<ModelDataType<?, ?>>> MODEL_DATA_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(
            Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "model_data_types")
    );

    public static final Registry<ModelDataType<?, ?>> MODEL_DATA_TYPE_REGISTRY = new RegistryBuilder<>(MODEL_DATA_TYPE_REGISTRY_KEY)
            .sync(true)
            .defaultKey(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "none"))
            .maxId(MODEL_DATA_TYPE_REGISTRY_MAX_ID)
            .create();
}
