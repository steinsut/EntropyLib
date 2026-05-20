package me.steinsut.entropylib.api.dyn.entity.sync;

import me.steinsut.entropylib.api.dyn.entity.sync.handler.IEntityDynSyncHandler;
import net.minecraft.world.level.storage.ValueOutput;

public class EntityDynSyncConfigReader {
    private final IEntityDynSyncHandler handler;

    public EntityDynSyncConfigReader(IEntityDynSyncHandler handler) {
        this.handler = handler;
    }

    public void writeConfiguration(ValueOutput output) {
        this.handler.writeConfiguration(output);
    }
}
