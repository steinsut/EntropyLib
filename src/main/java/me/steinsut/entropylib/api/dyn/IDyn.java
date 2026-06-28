/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn;

import me.steinsut.entropylib.api.dyn.data.DynDataWriter;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public interface IDyn<T extends BaseDynType<?, ?, S>, S extends BaseRenderState> {
    T getDynType();

    DynDataWriter<?> getDataWriter();
}
