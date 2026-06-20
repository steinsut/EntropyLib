/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.entity.helper;

import com.mojang.logging.LogUtils;
import me.steinsut.entropylib.api.dyn.data.DynDataType;
import me.steinsut.entropylib.api.dyn.data.DynDataWriter;
import me.steinsut.entropylib.api.dyn.entity.EntityDynType;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncConfigReader;
import me.steinsut.entropylib.api.dyn.entity.sync.DynEntitySyncPolicy;
import me.steinsut.entropylib.api.dyn.entity.sync.handler.IEntityDynSyncHandler;
import me.steinsut.entropylib.api.renderer.entity.DynEntityRenderState;
import me.steinsut.entropylib.network.ClientboundSetEntityDynType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import static me.steinsut.entropylib.api.registries.CommonRegistries.ENTITY_DYN_RENDERER_TYPE_REGISTRY;

public class DynEntityHelper<S extends DynEntityRenderState<S>> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String VALUE_IO_DYN_KEY = "dyn";
    public static final String VALUE_IO_DYN_RENDERER_TYPE_KEY = "r_type";
    public static final String VALUE_IO_DYN_DATA_KEY = "r_data";
    public static final String VALUE_IO_SYNC_POLICY_KEY = "s_pol";
    public static final String VALUE_IO_SYNC_CONF_KEY = "s_conf";

    private final Entity entity;
    private EntityDynType<?, ?, S> dynRendererType;
    private DynDataType.Holder<?> dynData;
    private DynEntitySyncPolicy dynSyncPolicy;
    private IEntityDynSyncHandler dynSyncHandler;

    public DynEntityHelper(Entity entity, EntityDynType<?, ?, S> dynRendererType, DynEntitySyncPolicy dynSyncPolicy) {
        this.entity = entity;
    }

    public EntityDynType<?, ?, S> getDynType() {
        return this.dynRendererType;
    }

    public void setDynRendererType(EntityDynType<?, ?, ?> type) {
        if (type.isCompatible(this.entity.typeHolder())) {
            //noinspection unchecked
            this.dynRendererType = (EntityDynType<?, ?, S>) type;
            this.dynData = type.getDataType().createHolder();

            if (!this.entity.level().isClientSide()) {
                PacketDistributor.sendToPlayersTrackingChunk(
                        (ServerLevel) this.entity.level(),
                        this.entity.chunkPosition(),
                        new ClientboundSetEntityDynType(this.entity.getId(), this.dynRendererType)
                );
            }
        } else {
            LOGGER.warn("DynRendererType {} is incompatible with entity type {}",
                    ENTITY_DYN_RENDERER_TYPE_REGISTRY.getKey(type),
                    this.entity.typeHolder().getKey().identifier());
        }
    }

    public DynEntitySyncPolicy getDynSyncPolicy() {
        return this.dynSyncPolicy;
    }

    public void setDynSyncPolicy(DynEntitySyncPolicy policy) {
        this.dynSyncPolicy = policy;
        this.dynSyncHandler = policy.create();
    }

    public DynDataWriter<?> getDynDataWriter() {
        return this.dynData.getWriter();
    }

    public DynEntitySyncConfigReader getDynSyncConfigurator() {
        return new DynEntitySyncConfigReader(this.dynSyncHandler);
    }

    private void dataUpdateSyncCheck(boolean forceSync) {
        if (!this.entity.level().isClientSide()) {
            this.dynSyncHandler.onDataUpdate();

            if (forceSync || this.dynSyncHandler.needsSync()) {
                PacketDistributor.sendToPlayersTrackingChunk(
                        (ServerLevel) this.entity.level(),
                        this.entity.chunkPosition(),
                        new ClientboundSetEntityDynType(this.entity.getId(), this.dynRendererType)
                );

                this.dynSyncHandler.reset();
            }
        }
    }

    public void readDataFrom(DynDataWriter<?> writer, boolean forceSync) {
        writer.writeToHolder(this.dynData);

        this.dataUpdateSyncCheck(forceSync);
    }

    public void setDataToPreset(Identifier id, boolean forceSync) {
        this.dynData.setToPreset(id);

        this.dataUpdateSyncCheck(forceSync);
    }

    public void readAdditionalSaveData(@NonNull ValueInput input) {
        input.child(VALUE_IO_DYN_KEY).ifPresent((dynChild) -> {
            dynChild.read(VALUE_IO_DYN_RENDERER_TYPE_KEY, EntityDynType.CODEC).ifPresent((type) -> {
                //noinspection unchecked
                this.dynRendererType = (EntityDynType<?, ?, S>) type;
                this.dynData = this.dynRendererType.getDataType().createHolder();

                this.dynData.getReader().readData(dynChild, VALUE_IO_DYN_DATA_KEY);
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

    public void addAdditionalSaveData(@NonNull ValueOutput output) {
        ValueOutput dynChild = output.child(VALUE_IO_DYN_KEY);

        if (this.dynRendererType != null) {
            dynChild.store(VALUE_IO_DYN_RENDERER_TYPE_KEY, EntityDynType.CODEC, this.dynRendererType);
            this.dynData.getWriter().storeData(dynChild, VALUE_IO_DYN_DATA_KEY);
        }

        dynChild.store(VALUE_IO_SYNC_POLICY_KEY, DynEntitySyncPolicy.CODEC, this.dynSyncPolicy);
        ValueOutput confChild = dynChild.child(VALUE_IO_SYNC_CONF_KEY);
        this.dynSyncHandler.writeConfiguration(confChild);
    }

    public void tick() {
        if (!this.entity.level().isClientSide()) {
            this.dynSyncHandler.onTick();
        }
    }
}
