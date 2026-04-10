package me.steinsut.entropylib.event.handlers;

import me.steinsut.entropylib.api.dynrenderer.entity.EntityDynRendererType;
import me.steinsut.entropylib.event.IModEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static me.steinsut.entropylib.api.registries.Registries.DYN_RENDERER_TYPE_REGISTRY;

public class ClientEventHandler implements IModEventHandler {
    @Override
    public void registerModEventHandlers(IEventBus bus) {
        bus.addListener(this::onAddEntityRendererLayers);
    }

    private void onAddEntityRendererLayers(final EntityRenderersEvent.AddLayers event) {
        DYN_RENDERER_TYPE_REGISTRY.forEach((t) -> {
            if (t instanceof EntityDynRendererType<?, ?, ?> et) {
                et.instantiateDynRenderer(event.getContext());
            }
        });
    }
}
