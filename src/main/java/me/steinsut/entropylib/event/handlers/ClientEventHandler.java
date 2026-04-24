package me.steinsut.entropylib.event.handlers;

import me.steinsut.entropylib.api.dyn.renderer.entity.EntityDynRendererType;
import me.steinsut.entropylib.event.IModEventHandler;
import me.steinsut.entropylib.network.ClientboundSetEntityDynRType;
import me.steinsut.entropylib.network.ClientboundUpdateEntityDynData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;

import static me.steinsut.entropylib.api.registries.CommonRegistries.DYN_RENDERER_TYPE_REGISTRY;

public class ClientEventHandler implements IModEventHandler {
    @Override
    public void registerModEventHandlers(IEventBus bus) {
        bus.addListener(this::onAddEntityRendererLayers);
        bus.addListener(this::registerPayloads);
    }

    private void onAddEntityRendererLayers(final EntityRenderersEvent.AddLayers event) {
        DYN_RENDERER_TYPE_REGISTRY.forEach((t) -> {
            if (t instanceof EntityDynRendererType<?, ?, ?> et) {
                et.instantiateDynRenderer(event.getContext());
            }
        });
    }

    private void registerPayloads(final RegisterClientPayloadHandlersEvent event) {
        event.register(
                ClientboundSetEntityDynRType.TYPE,
                HandlerThread.MAIN,
                ClientboundSetEntityDynRType::handleOnMain
        );

        event.register(
                ClientboundUpdateEntityDynData.TYPE,
                HandlerThread.MAIN,
                ClientboundUpdateEntityDynData::handleOnMain
        );
    }
}
