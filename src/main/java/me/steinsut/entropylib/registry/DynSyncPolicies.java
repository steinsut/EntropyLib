package me.steinsut.entropylib.registry;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.dyn.entity.sync.EntityDynSyncPolicy;
import net.neoforged.neoforge.registries.DeferredRegister;

import static me.steinsut.entropylib.api.registries.CommonRegistries.ENTITY_DYN_SYNC_POLICY_REGISTRY;

public class DynSyncPolicies {
    public static DeferredRegister<EntityDynSyncPolicy> POLICIES = DeferredRegister.create(
            ENTITY_DYN_SYNC_POLICY_REGISTRY,
            EntropyLibApi.MOD_ID
    );
}
