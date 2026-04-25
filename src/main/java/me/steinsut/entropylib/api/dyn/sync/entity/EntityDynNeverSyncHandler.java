package me.steinsut.entropylib.api.dyn.sync.entity;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EntityDynNeverSyncHandler implements IEntityDynSyncHandler {
    @Override
    public void onDataUpdate() {}

    @Override
    public void onTick() {}

    @Override
    public void reset() {}

    @Override
    public boolean needsSync() {
        return false;
    }

    @Override
    public void readConfiguration(ValueInput input) {}

    @Override
    public void writeConfiguration(ValueOutput output) {}
}
