package me.steinsut.entropylib.api.dyn.sync.entity;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface IEntityDynSyncHandler {
    void onDataUpdate();
    void onTick();
    void reset();

    boolean needsSync();

    void readConfiguration(ValueInput input);
    void writeConfiguration(ValueOutput output);
}
