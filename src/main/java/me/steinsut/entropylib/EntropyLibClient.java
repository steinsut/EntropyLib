package me.steinsut.entropylib;

import me.steinsut.entropylib.api.EntropyLibApi;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = EntropyLibApi.MOD_ID, dist = Dist.CLIENT)

@EventBusSubscriber(modid = EntropyLibApi.MOD_ID, value = Dist.CLIENT)
public class EntropyLibClient {
    public EntropyLibClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        EntropyLib.LOGGER.info("Client overengineering in progress...");
    }
}
