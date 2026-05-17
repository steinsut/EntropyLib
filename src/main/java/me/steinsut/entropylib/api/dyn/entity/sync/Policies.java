package me.steinsut.entropylib.api.dyn.entity.sync;

import net.minecraft.core.Holder;

import static me.steinsut.entropylib.registry.DynSyncPolicies.POLICIES;

public class Policies {
    public static Holder<EntityDynSyncPolicy> ALWAYS = POLICIES.register(
            "always",
            () -> new EntityDynSyncPolicy(EntityDynAlwaysSyncHandler::new)
    );

    public static Holder<EntityDynSyncPolicy> UPDATE_COUNT = POLICIES.register(
            "update_count",
            () -> new EntityDynSyncPolicy(EntityDynUpdateSyncHandler::new)
    );

    public static Holder<EntityDynSyncPolicy> TICK_COUNT = POLICIES.register(
            "tick_count",
            () -> new EntityDynSyncPolicy(EntityDynTickSyncHandler::new)
    );

    public static Holder<EntityDynSyncPolicy> NEVER = POLICIES.register(
            "never",
            () -> new EntityDynSyncPolicy(EntityDynNeverSyncHandler::new)
    );
}
