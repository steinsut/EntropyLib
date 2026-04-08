package me.steinsut.entropylib.event.handlers;

import me.steinsut.entropylib.api.registries.Registries;
import me.steinsut.entropylib.event.IModEventHandler;
import me.steinsut.entropylib.event.INeoEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import static me.steinsut.entropylib.EntropyLib.LOGGER;

public class CommonEventHandler implements IModEventHandler, INeoEventHandler {
    @Override
    public void registerModEventHandlers(IEventBus bus) {
        bus.addListener(this::createNewRegistries);
    }

    @Override
    public void registerNeoEventHandlers(IEventBus bus) {
        bus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Overengineering in progress...");
    }

    private void createNewRegistries(final NewRegistryEvent event) {
        event.register(Registries.DYN_RENDERER_DATA_TYPE_REGISTRY);
    }
}
