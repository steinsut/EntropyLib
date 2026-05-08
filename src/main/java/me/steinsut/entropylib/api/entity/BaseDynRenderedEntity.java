/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.entity;

import me.steinsut.entropylib.api.dyn.data.DynDataType;
import me.steinsut.entropylib.api.dyn.data.DynDataWriter;
import me.steinsut.entropylib.api.dyn.entity.IDynRenderedEntity;
import me.steinsut.entropylib.api.dyn.renderer.BaseDynRendererType;
import me.steinsut.entropylib.api.dyn.renderer.entity.EntityDynRendererType;
import me.steinsut.entropylib.api.renderer.entity.DynRenderedEntityRenderState;
import me.steinsut.entropylib.network.ClientboundSetEntityDynRType;
import me.steinsut.entropylib.api.dyn.entity.sync.EntityDynSyncPolicy;
import me.steinsut.entropylib.api.dyn.entity.sync.IEntityDynSyncHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;

import static me.steinsut.entropylib.EntropyLib.LOGGER;
import static me.steinsut.entropylib.api.registries.CommonRegistries.DYN_RENDERER_TYPE_REGISTRY;

public abstract class BaseDynRenderedEntity<S extends DynRenderedEntityRenderState<S>> extends Entity implements IDynRenderedEntity<S> {
    public static final String VALUE_IO_DYN_KEY = "dyn";
    public static final String VALUE_IO_DYN_RENDERER_TYPE_KEY = "r_type";
    public static final String VALUE_IO_DYN_DATA_KEY = "r_data";
    public static final String VALUE_IO_SYNC_POLICY_KEY = "s_pol";
    public static final String VALUE_IO_SYNC_CONF_KEY = "s_conf";

    protected EntityDynRendererType<?, ?, S> dynRendererType;
    protected DynDataType.Holder<?> dynData;
    protected EntityDynSyncPolicy dynSyncPolicy;
    protected IEntityDynSyncHandler dynSyncHandler;

    public BaseDynRenderedEntity(EntityType<?> type, Level level, EntityDynRendererType<?, ?, S> dynRendererType, EntityDynSyncPolicy dynSyncPolicy) {
        super(type, level);

        this.setDynRendererType(dynRendererType);
        this.setDynSyncPolicy(dynSyncPolicy);
    }

    @Override
    public EntityDynRendererType<?, ?, S> getDynRendererType() {
        return this.dynRendererType;
    }

    @Override
    public DynDataWriter<?> getDynDataWriter() {
        return this.dynData.getWriter();
    }

    @Override
    public void setDynRendererType(EntityDynRendererType<?, ?, ?> type) {
        if (type.isCompatible(this.typeHolder())) {
            //noinspection unchecked
            this.dynRendererType = (EntityDynRendererType<?, ?, S>) type;
            this.dynData = type.getDataType().createHolder();

            if (!this.level().isClientSide()) {
                PacketDistributor.sendToPlayersTrackingChunk(
                        (ServerLevel) this.level(),
                        this.chunkPosition(),
                        new ClientboundSetEntityDynRType(this.getId(), this.dynRendererType)
                );
            }
        } else {
            LOGGER.warn("DynRendererType {} is incompatible with entity type {}",
                    DYN_RENDERER_TYPE_REGISTRY.getKey(type),
                    this.typeHolder().getKey().identifier());
        }
    }

    @Override
    public void setDynSyncPolicy(EntityDynSyncPolicy policy) {
        this.dynSyncPolicy = policy;
        this.dynSyncHandler = policy.create();
    }

    @Override
    public void readDataFrom(DynDataWriter<?> writer) {
        writer.writeToHolder(this.dynData);

        if (this.level().isClientSide()) {
            this.dynSyncHandler.onDataUpdate();

            if (this.dynSyncHandler.needsSync()) {
                PacketDistributor.sendToPlayersTrackingChunk(
                        (ServerLevel) this.level(),
                        this.chunkPosition(),
                        new ClientboundSetEntityDynRType(this.getId(), this.dynRendererType)
                );

                this.dynSyncHandler.reset();
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        input.child(VALUE_IO_DYN_KEY).ifPresent((dynChild) -> {
            dynChild.read(VALUE_IO_DYN_RENDERER_TYPE_KEY, EntityDynRendererType.CODEC).ifPresent((type) -> {
                //noinspection unchecked
                this.dynRendererType = (EntityDynRendererType<?, ?, S>) type;
                this.dynData = this.dynRendererType.getDataType().createHolder();

                this.dynData.getReader().readData(dynChild);
            });

            dynChild.read(VALUE_IO_SYNC_POLICY_KEY, EntityDynSyncPolicy.CODEC).ifPresent((policy) -> {
                this.dynSyncPolicy = policy;
                this.dynSyncHandler = policy.create();

                dynChild.child(VALUE_IO_SYNC_CONF_KEY).ifPresent((configuration) -> {
                    this.dynSyncHandler.readConfiguration(configuration);
                });
            });
        });
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        ValueOutput dynChild = output.child(VALUE_IO_DYN_KEY);

        if (this.dynRendererType != null) {
            dynChild.store(VALUE_IO_DYN_RENDERER_TYPE_KEY, EntityDynRendererType.CODEC, this.dynRendererType);
            this.dynData.getWriter().storeData(dynChild);
        }

        dynChild.store(VALUE_IO_SYNC_POLICY_KEY, EntityDynSyncPolicy.CODEC, this.dynSyncPolicy);
        ValueOutput confChild = dynChild.child(VALUE_IO_SYNC_CONF_KEY);
        this.dynSyncHandler.writeConfiguration(confChild);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            this.dynSyncHandler.onTick();
        }
    }
}
