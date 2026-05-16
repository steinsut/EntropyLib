/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.entity.sync;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class EntityDynTickSyncHandler implements IEntityDynSyncHandler {
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
    public void onDataUpdate() {
    }

    @Override
    public void onTick() {
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