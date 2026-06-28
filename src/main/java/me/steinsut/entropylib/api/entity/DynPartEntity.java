/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.entity;

import me.steinsut.entropylib.api.dyn.data.DynDataWriter;
import me.steinsut.entropylib.api.dyn.entity.helper.DynEntityHelper;
import me.steinsut.entropylib.api.dyn.entity.EntityDynType;
import me.steinsut.entropylib.api.dyn.entity.IDynEntity;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncConfigReader;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncPolicy;
import me.steinsut.entropylib.api.renderer.entity.DynEntityRenderState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.entity.PartEntity;
import org.jspecify.annotations.NonNull;

public abstract class DynPartEntity<T extends Entity, S extends DynEntityRenderState<S>> extends PartEntity<T> implements IDynEntity<S>, IEntityWithComplexSpawn {
    protected final DynEntityHelper<S> dynHelper;

    public DynPartEntity(T parent, EntityDynType<?, ?, S> dynRendererType, DynEntitySyncPolicy dynSyncPolicy) {
        super(parent);

        this.dynHelper = new DynEntityHelper<>(this);
        this.dynHelper.init(dynRendererType, dynSyncPolicy);
    }

    @Override
    public final EntityDynType<?, ?, S> getDynType() {
        return this.dynHelper.getDynType();
    }

    @Override
    public final void setDynType(EntityDynType<?, ?, ?> dynType) {
        this.dynHelper.setDynType(dynType);
    }

    @Override
    public final DynEntitySyncPolicy getSyncPolicy() {
        return this.dynHelper.getDynSyncPolicy();
    }

    @Override
    public final void setSyncPolicy(DynEntitySyncPolicy policy) {
        this.dynHelper.setDynSyncPolicy(policy);
    }

    @Override
    public final DynDataWriter<?> getDataWriter() {
        return this.dynHelper.getDynDataWriter();
    }

    @Override
    public final DynEntitySyncConfigReader getSyncConfigurator() {
        return this.dynHelper.getDynSyncConfigurator();
    }

    @Override
    public final void readDataFrom(DynDataWriter<?> writer, boolean forceSync) {
        this.dynHelper.readDataFrom(writer, forceSync);
    }

    @Override
    public final void setDataToPreset(Identifier presetId, boolean forceSync) {
        this.dynHelper.setDataToPreset(presetId, forceSync);
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        this.dynHelper.readAdditionalSaveData(input);
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        this.dynHelper.addAdditionalSaveData(output);
    }

    @Override
    public void readSpawnData(@NonNull RegistryFriendlyByteBuf buffer) {
        this.dynHelper.readSpawnData(buffer);
    }

    @Override
    public void writeSpawnData(@NonNull RegistryFriendlyByteBuf buffer) {
        this.dynHelper.writeSpawnData(buffer);
    }

    @Override
    public void tick() {
        super.tick();

        this.dynHelper.tick();
    }
}
