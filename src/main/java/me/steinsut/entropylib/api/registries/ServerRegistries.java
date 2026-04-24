package me.steinsut.entropylib.api.registries;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.network.sync.EntityDynSyncPolicy;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ServerRegistries {
    private static final int ENTITY_DYN_SYNC_POLICY_MAX_ID = 255;

    public static final ResourceKey<Registry<EntityDynSyncPolicy>> ENTITY_DYN_SYNC_POLICY_REGISTRY_KEY = ResourceKey.createRegistryKey(
            Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "sync_policies")
    );

    public static final Registry<EntityDynSyncPolicy> ENTITY_DYN_SYNC_POLICY_REGISTRY = new RegistryBuilder<>(ENTITY_DYN_SYNC_POLICY_REGISTRY_KEY)
            .sync(false)
            .defaultKey(Identifier.fromNamespaceAndPath(EntropyLibApi.MOD_ID, "none"))
            .maxId(ENTITY_DYN_SYNC_POLICY_MAX_ID)
            .create();
}
