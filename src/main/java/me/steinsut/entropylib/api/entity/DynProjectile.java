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
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

public abstract class DynProjectile<S extends DynEntityRenderState<S>> extends Projectile implements IDynEntity<S> {
    protected final DynEntityHelper<S> dynHelper;

    public DynProjectile(EntityType<? extends Projectile> type, Level level, EntityDynType<?, ?, S> dynRendererType, DynEntitySyncPolicy dynSyncPolicy) {
        super(type, level);

        this.dynHelper = new DynEntityHelper<>(this, dynRendererType, dynSyncPolicy);

        this.setDynType(dynRendererType);
        this.setDynSyncPolicy(dynSyncPolicy);
    }

    @Override
    public EntityDynType<?, ?, S> getDynType() {
        return this.dynHelper.getDynType();
    }

    @Override
    public void setDynType(EntityDynType<?, ?, ?> dynType) {
        this.dynHelper.setDynRendererType(dynType);
    }

    @Override
    public DynEntitySyncPolicy getDynSyncPolicy() {
        return this.dynHelper.getDynSyncPolicy();
    }

    @Override
    public void setDynSyncPolicy(DynEntitySyncPolicy policy) {
        this.dynHelper.setDynSyncPolicy(policy);
    }

    @Override
    public DynDataWriter<?> getDynDataWriter() {
        return this.dynHelper.getDynDataWriter();
    }

    @Override
    public DynEntitySyncConfigReader getDynSyncConfigurator() {
        return this.dynHelper.getDynSyncConfigurator();
    }

    @Override
    public void readDynDataFrom(DynDataWriter<?> writer, boolean forceSync) {
        this.dynHelper.readDataFrom(writer, forceSync);
    }

    @Override
    public void setDynDataToPreset(Identifier presetId, boolean forceSync) {
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
    public void tick() {
        super.tick();

        this.dynHelper.tick();
    }
}
