package me.steinsut.entropylib.network.sync;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EntityDynAlwaysSyncHandler implements IEntityDynSyncHandler {
    @Override
    public void onDataUpdate() {}

    @Override
    public void onEntityTick() {}

    @Override
    public void reset() {}

    @Override
    public boolean needsSync() {
        return true;
    }

    @Override
    public void readConfiguration(ValueInput input) {}

    @Override
    public void writeConfiguration(ValueOutput output) {}
}