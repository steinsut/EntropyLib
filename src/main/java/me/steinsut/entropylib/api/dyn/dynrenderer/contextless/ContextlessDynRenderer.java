/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.dynrenderer.contextless;

import me.steinsut.entropylib.api.dyn.data.DynDataType;
import me.steinsut.entropylib.api.dyn.dynrenderer.DynRenderer;
import me.steinsut.entropylib.util.Cloneable;
import net.neoforged.neoforge.client.renderstate.BaseRenderState;

public abstract class ContextlessDynRenderer<D extends Cloneable<D>, S extends BaseRenderState> extends DynRenderer<D, S> {
    public ContextlessDynRenderer(DynDataType<D> dataType) {
        super(dataType);
    }
}
