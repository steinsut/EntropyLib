package me.steinsut.entropylib.network.sync;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface IEntityDynSyncHandler {
    void onDataUpdate();
    void onEntityTick();
    void reset();

    boolean needsSync();

    void readConfiguration(ValueInput input);
    void writeConfiguration(ValueOutput output);
}
