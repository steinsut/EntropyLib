package me.steinsut.entropylib.event;

import net.neoforged.bus.api.IEventBus;

public interface IModEventHandler {
    void registerModEventHandlers(final IEventBus bus);
}
