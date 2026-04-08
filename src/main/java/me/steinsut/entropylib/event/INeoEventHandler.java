package me.steinsut.entropylib.event;

import net.neoforged.bus.api.IEventBus;

public interface INeoEventHandler {
    void registerNeoEventHandlers(final IEventBus bus);
}
