package me.steinsut.entropylib.event.handlers;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.registries.Registries;
import me.steinsut.entropylib.event.IModEventHandler;
import me.steinsut.entropylib.event.INeoEventHandler;
import me.steinsut.entropylib.network.ClientboundSetEntityDynRType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.function.Function;

import static me.steinsut.entropylib.EntropyLib.LOGGER;

public class CommonEventHandler implements IModEventHandler, INeoEventHandler {
    @Override
    public void registerModEventHandlers(IEventBus bus) {
        bus.addListener(this::createNewRegistries);
        bus.addListener(this::registerPayloads);
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
        event.register(Registries.DYN_RENDERER_TYPE_REGISTRY);
    }

    private void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(EntropyLibApi.NETWORK_PROTOCOL_VERSION);

        registrar.playToClient(
                ClientboundSetEntityDynRType.TYPE,
                ClientboundSetEntityDynRType.STREAM_CODEC,
                ClientboundSetEntityDynRType::handleOnMain
        );
    }
}
