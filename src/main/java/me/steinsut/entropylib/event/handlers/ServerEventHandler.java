package me.steinsut.entropylib.event.handlers;

import me.steinsut.entropylib.event.IModEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public class ServerEventHandler implements IModEventHandler {
    @Override
    public void registerModEventHandlers(IEventBus bus) {
        bus.addListener(this::createNewRegistries);
    }

    private void createNewRegistries(final NewRegistryEvent event) {

    }
}
