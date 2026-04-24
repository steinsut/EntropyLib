package me.steinsut.entropylib;

import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.event.handlers.ClientEventHandler;
import me.steinsut.entropylib.event.handlers.ServerEventHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static me.steinsut.entropylib.EntropyLib.LOGGER;

@Mod(value = EntropyLibApi.MOD_ID, dist = Dist.DEDICATED_SERVER)
public class EntropyLibServer {
    public EntropyLibServer(IEventBus modEventBus, ModContainer container) {
        LOGGER.info("Hello from EntropyLib Server! Overengineering in progress...");

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        ServerEventHandler serverEventHandler = new ServerEventHandler();
        serverEventHandler.registerModEventHandlers(modEventBus);
    }
}
