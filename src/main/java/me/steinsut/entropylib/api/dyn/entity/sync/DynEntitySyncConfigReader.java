package me.steinsut.entropylib.api.dyn.entity.sync;

import me.steinsut.entropylib.api.dyn.entity.sync.handler.IEntityDynSyncHandler;
import net.minecraft.world.level.storage.ValueOutput;

public class DynEntitySyncConfigReader {
    private final IEntityDynSyncHandler handler;

    public DynEntitySyncConfigReader(IEntityDynSyncHandler handler) {
        this.handler = handler;
    }

    public void writeConfiguration(ValueOutput output) {
        this.handler.writeConfiguration(output);
    }
}
