/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.event.handlers;

import com.mojang.logging.LogUtils;
import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.network.payload.ServerboundRequestEntityDynData;
import me.steinsut.entropylib.api.network.payload.ServerboundRequestEntityDynType;
import me.steinsut.entropylib.api.registries.CommonRegistries;
import me.steinsut.entropylib.commands.EntropyLibCommands;
import me.steinsut.entropylib.network.payload.ClientboundSetEntityDynType;
import me.steinsut.entropylib.network.payload.ClientboundUpdateEntityDynData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

import static me.steinsut.entropylib.registry.DynEntitySyncPolicies.POLICIES;

public class CommonEventHandler implements ModEventHandler, NeoEventHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void registerModEventHandlers(IEventBus bus) {
        POLICIES.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::createNewRegistries);
        bus.addListener(this::registerPayloads);
    }

    @Override
    public void registerNeoEventHandlers(IEventBus bus) {
        bus.addListener(this::registerCommands);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Overengineering in progress...");
    }

    private void createNewRegistries(final NewRegistryEvent event) {
        event.register(CommonRegistries.DYN_DATA_TYPE_REGISTRY);
        event.register(CommonRegistries.ENTITY_DYN_TYPE_REGISTRY);
        event.register(CommonRegistries.DYN_ENTITY_SYNC_POLICY_REGISTRY);
    }

    private void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(EntropyLibApi.NETWORK_PROTOCOL_VERSION);

        final PayloadRegistrar mainRegistrar = registrar.executesOn(HandlerThread.MAIN);
        final PayloadRegistrar networkRegistrar = registrar.executesOn(HandlerThread.NETWORK);

        mainRegistrar.playToClient(
                ClientboundSetEntityDynType.TYPE,
                ClientboundSetEntityDynType.STREAM_CODEC
        );

        mainRegistrar.playToClient(
                ClientboundUpdateEntityDynData.TYPE,
                ClientboundUpdateEntityDynData.STREAM_CODEC
        );

        mainRegistrar.playToServer(
                ServerboundRequestEntityDynType.TYPE,
                ServerboundRequestEntityDynType.STREAM_CODEC,
                ServerboundRequestEntityDynType::handleOnMain
        );

        mainRegistrar.playToServer(
                ServerboundRequestEntityDynData.TYPE,
                ServerboundRequestEntityDynData.STREAM_CODEC,
                ServerboundRequestEntityDynData::handleOnMain
        );
    }

    private void registerCommands(final RegisterCommandsEvent event) {
        EntropyLibCommands.register(event.getDispatcher(), event.getBuildContext());
    }
}
