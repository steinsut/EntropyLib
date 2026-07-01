/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.entity;

import me.steinsut.entropylib.api.dyn.Dyn;
import me.steinsut.entropylib.api.dyn.data.DynDataWriter;
import me.steinsut.entropylib.api.dyn.dynrenderer.contextless.ContextlessDynRenderer;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncConfigReader;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncPolicy;
import me.steinsut.entropylib.api.renderer.entity.DynEntityRenderState;
import net.minecraft.resources.Identifier;

public interface DynEntity<S extends DynEntityRenderState<S>> extends Dyn<EntityDynType<?, ?, S>, S> {
    void setDynType(EntityDynType<?, ?, ?> dynType);

    DynEntitySyncPolicy getSyncPolicy();

    void setSyncPolicy(DynEntitySyncPolicy policy);

    DynEntitySyncConfigReader getSyncConfigurator();

    ContextlessDynRenderer<?, ? super S> getFallbackDynRenderer();

    void readDataFrom(DynDataWriter<?> writer, boolean forceSync);

    void setDataToPreset(Identifier preset, boolean forceSync);
}
