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

public class EntityDynAlwaysSyncHandler implements IEntityDynSyncHandler {
    @Override
    public void onDataUpdate() {
    }

    @Override
    public void onTick() {
    }

    @Override
    public void reset() {
    }

    @Override
    public boolean needsSync() {
        return true;
    }

    @Override
    public void readConfiguration(ValueInput input) {
    }

    @Override
    public void writeConfiguration(ValueOutput output) {
    }
}