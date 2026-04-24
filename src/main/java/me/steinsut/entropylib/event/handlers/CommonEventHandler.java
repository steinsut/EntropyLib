package me.steinsut.entropylib.event.handlers;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.api.registries.CommonRegistries;
import me.steinsut.entropylib.event.IModEventHandler;
import me.steinsut.entropylib.event.INeoEventHandler;
import me.steinsut.entropylib.network.ClientboundSetEntityDynRType;
import me.steinsut.entropylib.network.ClientboundUpdateEntityDynRData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import static me.steinsut.entropylib.EntropyLib.LOGGER;

public class CommonEventHandler implements IModEventHandler, INeoEventHandler {
    @Override
    public void registerModEventHandlers(IEventBus bus) {
        bus.addListener(this::commonSetup);
        bus.addListener(this::createNewRegistries);
        bus.addListener(this::registerPayloads);
    }

    @Override
    public void registerNeoEventHandlers(IEventBus bus) {

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Overengineering in progress...");
    }

    private void createNewRegistries(final NewRegistryEvent event) {
        event.register(CommonRegistries.DYN_RENDERER_DATA_TYPE_REGISTRY);
        event.register(CommonRegistries.DYN_RENDERER_TYPE_REGISTRY);
    }

    private void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(EntropyLibApi.NETWORK_PROTOCOL_VERSION);

        final PayloadRegistrar mainRegistrar = registrar.executesOn(HandlerThread.MAIN);
        final PayloadRegistrar networkRegistrar = registrar.executesOn(HandlerThread.NETWORK);

        mainRegistrar.playToClient(
                ClientboundSetEntityDynRType.TYPE,
                ClientboundSetEntityDynRType.STREAM_CODEC
        );

        mainRegistrar.playToClient(
                ClientboundUpdateEntityDynRData.TYPE,
                ClientboundUpdateEntityDynRData.STREAM_CODEC
        );
    }
}
