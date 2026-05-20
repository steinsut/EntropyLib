package me.steinsut.entropylib.api.dyn.entity.sync;

import me.steinsut.entropylib.api.dyn.entity.sync.handler.EntityDynAlwaysSyncHandler;
import me.steinsut.entropylib.api.dyn.entity.sync.handler.EntityDynNeverSyncHandler;
import me.steinsut.entropylib.api.dyn.entity.sync.handler.EntityDynTickSyncHandler;
import me.steinsut.entropylib.api.dyn.entity.sync.handler.EntityDynUpdateSyncHandler;
import net.minecraft.core.Holder;

import static me.steinsut.entropylib.registry.DynEntitySyncPolicies.POLICIES;

public class Policies {
    public static Holder<DynEntitySyncPolicy> ALWAYS = POLICIES.register(
            "always",
            () -> new DynEntitySyncPolicy(EntityDynAlwaysSyncHandler::new)
    );

    public static Holder<DynEntitySyncPolicy> UPDATE_COUNT = POLICIES.register(
            "update_count",
            () -> new DynEntitySyncPolicy(EntityDynUpdateSyncHandler::new)
    );

    public static Holder<DynEntitySyncPolicy> TICK_COUNT = POLICIES.register(
            "tick_count",
            () -> new DynEntitySyncPolicy(EntityDynTickSyncHandler::new)
    );

    public static Holder<DynEntitySyncPolicy> NEVER = POLICIES.register(
            "never",
            () -> new DynEntitySyncPolicy(EntityDynNeverSyncHandler::new)
    );
}
