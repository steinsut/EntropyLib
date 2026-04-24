package me.steinsut.entropylib.network.sync;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EntityDynTickSyncHandler implements EntityDynSyncHandler {
    public static final int DEFAULT_TICKS_PER_SYNC = 20;

    private int ticksPerSync;
    private int tickCount;

    public EntityDynTickSyncHandler() {
        this(DEFAULT_TICKS_PER_SYNC);
    }

    public EntityDynTickSyncHandler(int ticksPerSync) {
        this.ticksPerSync = ticksPerSync;
        this.tickCount = 0;
    }

    @Override
    public void onDataUpdate() {}

    @Override
    public void onEntityTick() {
        this.tickCount++;
    }

    @Override
    public void reset() {
        this.tickCount = 0;
    }

    @Override
    public boolean needsSync() {
        return this.tickCount >= this.ticksPerSync;
    }

    @Override
    public void readConfiguration(ValueInput input) {
        this.ticksPerSync = input.getIntOr("th", DEFAULT_TICKS_PER_SYNC);
    }

    @Override
    public void writeConfiguration(ValueOutput output) {
        output.putInt("th", this.ticksPerSync);
    }
}