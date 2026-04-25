package me.steinsut.entropylib.api.dyn.sync.entity;

import com.mojang.serialization.Codec;

import java.util.function.Supplier;

import static me.steinsut.entropylib.api.registries.ServerRegistries.ENTITY_DYN_SYNC_POLICY_REGISTRY;

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
