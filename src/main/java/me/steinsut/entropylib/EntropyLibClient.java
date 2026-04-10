package me.steinsut.entropylib;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.event.handlers.ClientEventHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static me.steinsut.entropylib.EntropyLib.LOGGER;

@Mod(value = EntropyLibApi.MOD_ID, dist = Dist.CLIENT)
public class EntropyLibClient {

    public EntropyLibClient(IEventBus modEventBus, ModContainer container) {
        LOGGER.info("Hello from EntropyLib Client! Overengineering in progress...");

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        ClientEventHandler clientEventHandler = new ClientEventHandler();
        clientEventHandler.registerModEventHandlers(modEventBus);
    }
}
