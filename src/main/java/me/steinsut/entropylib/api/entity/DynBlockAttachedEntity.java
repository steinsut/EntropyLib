/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.entity;

import me.steinsut.entropylib.api.dyn.data.DynDataWriter;
import me.steinsut.entropylib.api.dyn.entity.DynEntityHelper;
import me.steinsut.entropylib.api.dyn.entity.IDynEntity;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncConfigReader;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncPolicy;
import me.steinsut.entropylib.api.dyn.entity.DynEntityType;
import me.steinsut.entropylib.api.renderer.entity.DynEntityRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

public abstract class DynBlockAttachedEntity<S extends DynEntityRenderState<S>> extends BlockAttachedEntity implements IDynEntity<S> {
    protected final DynEntityHelper<S> helper;

    public DynBlockAttachedEntity(EntityType<? extends BlockAttachedEntity> type, Level level, DynEntityType<?, ?, S> dynRendererType, DynEntitySyncPolicy dynSyncPolicy) {
        super(type, level);

        this.helper = new DynEntityHelper<>(this, dynRendererType, dynSyncPolicy);

        this.setDynRendererType(dynRendererType);
        this.setDynSyncPolicy(dynSyncPolicy);
    }

    @Override
    public DynEntityType<?, ?, S> getDynType() {
        return this.helper.getDynType();
    }

    @Override
    public void setDynRendererType(DynEntityType<?, ?, ?> type) {
        this.helper.setDynRendererType(type);
    }

    @Override
    public DynEntitySyncPolicy getDynSyncPolicy() {
        return this.helper.getDynSyncPolicy();
    }

    @Override
    public void setDynSyncPolicy(DynEntitySyncPolicy policy) {
        this.helper.setDynSyncPolicy(policy);
    }

    @Override
    public DynDataWriter<?> getDynDataWriter() {
        return this.helper.getDynDataWriter();
    }

    @Override
    public DynEntitySyncConfigReader getDynSyncConfigurator() {
        return this.helper.getDynSyncConfigurator();
    }

    @Override
    public void readDataFrom(DynDataWriter<?> writer, boolean forceSync) {
        this.helper.readDataFrom(writer, forceSync);
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        this.helper.readAdditionalSaveData(input);
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        this.helper.addAdditionalSaveData(output);
    }

    @Override
    public void tick() {
        super.tick();

        this.helper.tick();
    }
}
