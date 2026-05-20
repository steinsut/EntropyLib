/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.entity;

import me.steinsut.entropylib.api.dyn.IDynRendered;
import me.steinsut.entropylib.api.dyn.contextless.ContextlessDynRenderer;
import me.steinsut.entropylib.api.dyn.data.DynDataWriter;
import me.steinsut.entropylib.api.dyn.entity.sync.EntityDynSyncPolicy;
import me.steinsut.entropylib.api.dyn.entity.sync.EntityDynSyncConfigReader;
import me.steinsut.entropylib.api.dyn.renderer.entity.EntityDynRendererType;
import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;

public interface IDynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends IDynRendered<S> {
    EntityDynRendererType<?, ?, S> getDynRendererType();

    void setDynRendererType(EntityDynRendererType<?, ?, ?> dynRendererType);

    EntityDynSyncPolicy getDynSyncPolicy();

    void setDynSyncPolicy(EntityDynSyncPolicy policy);

    EntityDynSyncConfigReader getDynSyncConfigurator();

    ContextlessDynRenderer<?, ? super S> getFallbackDynRenderer();

    void readDataFrom(DynDataWriter<?> writer, boolean forceSync);
}
