/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.event.handlers;

import me.steinsut.entropylib.event.IModEventHandler;
import me.steinsut.entropylib.network.ClientboundSetEntityDynType;
import me.steinsut.entropylib.network.ClientboundUpdateEntityDynData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;

import static me.steinsut.entropylib.api.registries.CommonRegistries.ENTITY_DYN_RENDERER_TYPE_REGISTRY;

public class ClientEventHandler implements IModEventHandler {
    @Override
    public void registerModEventHandlers(IEventBus bus) {
        bus.addListener(this::onAddEntityRendererLayers);
        bus.addListener(this::registerPayloads);
    }

    private void onAddEntityRendererLayers(final EntityRenderersEvent.AddLayers event) {
        ENTITY_DYN_RENDERER_TYPE_REGISTRY.forEach((t) -> t.instantiateDynRenderer(event.getContext()));
    }

    private void registerPayloads(final RegisterClientPayloadHandlersEvent event) {
        event.register(
                ClientboundSetEntityDynType.TYPE,
                HandlerThread.MAIN,
                ClientboundSetEntityDynType::handleOnMain
        );

        event.register(
                ClientboundUpdateEntityDynData.TYPE,
                HandlerThread.MAIN,
                ClientboundUpdateEntityDynData::handleOnMain
        );
    }
}
