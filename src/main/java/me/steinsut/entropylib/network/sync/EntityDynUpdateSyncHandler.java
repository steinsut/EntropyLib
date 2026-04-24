package me.steinsut.entropylib.network.sync;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EntityDynUpdateSyncHandler implements IEntityDynSyncHandler {
    public static final int DEFAULT_UPDATES_PER_SYNC = 10;

    private int updatesPerSync;
    private int updateCount;

    public EntityDynUpdateSyncHandler() {
        this(DEFAULT_UPDATES_PER_SYNC);
    }

    public EntityDynUpdateSyncHandler(int updatesPerSync) {
        this.updatesPerSync = updatesPerSync;
        this.updateCount = 0;
    }

    @Override
    public void onDataUpdate() {
        this.updateCount++;
    }

    @Override
    public void onTick() {}

    @Override
    public void reset() {
        this.updateCount = 0;
    }

    @Override
    public boolean needsSync() {
        return this.updateCount >= this.updatesPerSync;
    }

    @Override
    public void readConfiguration(ValueInput input) {
        this.updatesPerSync = input.getIntOr("th", DEFAULT_UPDATES_PER_SYNC);
    }

    @Override
    public void writeConfiguration(ValueOutput output) {
        output.putInt("th", this.updatesPerSync);
    }
}