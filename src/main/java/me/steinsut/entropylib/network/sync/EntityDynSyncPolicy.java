package me.steinsut.entropylib.network.sync;

import com.mojang.serialization.Codec;

import java.util.function.Supplier;

import static me.steinsut.entropylib.api.registries.ServerRegistries.ENTITY_DYN_SYNC_POLICY_REGISTRY;

public class EntityDynSyncPolicy {
    public static Codec<EntityDynSyncPolicy> CODEC = ENTITY_DYN_SYNC_POLICY_REGISTRY.byNameCodec();

    private final Supplier<EntityDynSyncHandler> handlerSupplier;

    public EntityDynSyncPolicy(Supplier<EntityDynSyncHandler> handlerSupplier) {
        this.handlerSupplier = handlerSupplier;
    }

    public EntityDynSyncHandler create() {
        return this.handlerSupplier.get();
    }
}
