/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.sync.entity;

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