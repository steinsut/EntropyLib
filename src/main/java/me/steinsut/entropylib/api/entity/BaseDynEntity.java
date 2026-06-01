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
import me.steinsut.entropylib.api.dyn.entity.IDynEntity;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncConfigReader;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncPolicy;
import me.steinsut.entropylib.api.dyn.entity.sync.handler.IEntityDynSyncHandler;
import me.steinsut.entropylib.api.dyn.renderer.entity.DynEntityRendererType;
import me.steinsut.entropylib.api.renderer.entity.DynEntityRenderState;
import me.steinsut.entropylib.network.ClientboundSetEntityDynType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;

import static me.steinsut.entropylib.EntropyLib.LOGGER;
import static me.steinsut.entropylib.api.registries.CommonRegistries.DYN_ENTITY_RENDERER_TYPE_REGISTRY;

public abstract class BaseDynEntity<S extends DynEntityRenderState<S>> extends Entity implements IDynEntity<S> {
    public static final String VALUE_IO_DYN_KEY = "dyn";
    public static final String VALUE_IO_DYN_RENDERER_TYPE_KEY = "r_type";
    public static final String VALUE_IO_DYN_DATA_KEY = "r_data";
    public static final String VALUE_IO_SYNC_POLICY_KEY = "s_pol";
    public static final String VALUE_IO_SYNC_CONF_KEY = "s_conf";

    protected DynEntityRendererType<?, ?, S> dynRendererType;
    protected DynDataType.Holder<?> dynData;
    protected DynEntitySyncPolicy dynSyncPolicy;
    protected IEntityDynSyncHandler dynSyncHandler;

    public BaseDynEntity(EntityType<?> type, Level level, DynEntityRendererType<?, ?, S> dynRendererType, DynEntitySyncPolicy dynSyncPolicy) {
        super(type, level);

        this.setDynRendererType(dynRendererType);
        this.setDynSyncPolicy(dynSyncPolicy);
    }

    @Override
    public DynEntityRendererType<?, ?, S> getDynType() {
        return this.dynRendererType;
    }

    @Override
    public void setDynRendererType(DynEntityRendererType<?, ?, ?> type) {
        if (type.isCompatible(this.typeHolder())) {
            //noinspection unchecked
            this.dynRendererType = (DynEntityRendererType<?, ?, S>) type;
            this.dynData = type.getDataType().createHolder();

            if (!this.level().isClientSide()) {
                PacketDistributor.sendToPlayersTrackingChunk(
                        (ServerLevel) this.level(),
                        this.chunkPosition(),
                        new ClientboundSetEntityDynType(this.getId(), this.dynRendererType)
                );
            }
        } else {
            LOGGER.warn("DynRendererType {} is incompatible with entity type {}",
                    DYN_ENTITY_RENDERER_TYPE_REGISTRY.getKey(type),
                    this.typeHolder().getKey().identifier());
        }
    }

    @Override
    public DynEntitySyncPolicy getDynSyncPolicy() {
        return this.dynSyncPolicy;
    }

    @Override
    public void setDynSyncPolicy(DynEntitySyncPolicy policy) {
        this.dynSyncPolicy = policy;
        this.dynSyncHandler = policy.create();
    }

    @Override
    public DynDataWriter<?> getDynDataWriter() {
        return this.dynData.getWriter();
    }

    @Override
    public DynEntitySyncConfigReader getDynSyncConfigurator() {
        return new DynEntitySyncConfigReader(this.dynSyncHandler);
    }

    @Override
    public void readDataFrom(DynDataWriter<?> writer, boolean forceSync) {
        writer.writeToHolder(this.dynData);

        if (!this.level().isClientSide()) {
            this.dynSyncHandler.onDataUpdate();

            if (forceSync || this.dynSyncHandler.needsSync()) {
                PacketDistributor.sendToPlayersTrackingChunk(
                        (ServerLevel) this.level(),
                        this.chunkPosition(),
                        new ClientboundSetEntityDynType(this.getId(), this.dynRendererType)
                );

                this.dynSyncHandler.reset();
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        input.child(VALUE_IO_DYN_KEY).ifPresent((dynChild) -> {
            dynChild.read(VALUE_IO_DYN_RENDERER_TYPE_KEY, DynEntityRendererType.CODEC).ifPresent((type) -> {
                //noinspection unchecked
                this.dynRendererType = (DynEntityRendererType<?, ?, S>) type;
                this.dynData = this.dynRendererType.getDataType().createHolder();

                this.dynData.getReader().readData(dynChild, BaseDynEntity.VALUE_IO_DYN_DATA_KEY);
            });

            dynChild.read(VALUE_IO_SYNC_POLICY_KEY, DynEntitySyncPolicy.CODEC).ifPresent((policy) -> {
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
            dynChild.store(VALUE_IO_DYN_RENDERER_TYPE_KEY, DynEntityRendererType.CODEC, this.dynRendererType);
            this.dynData.getWriter().storeData(dynChild, BaseDynEntity.VALUE_IO_DYN_DATA_KEY);
        }

        dynChild.store(VALUE_IO_SYNC_POLICY_KEY, DynEntitySyncPolicy.CODEC, this.dynSyncPolicy);
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
