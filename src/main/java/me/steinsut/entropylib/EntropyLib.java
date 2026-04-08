package me.steinsut.entropylib;

import com.mojang.logging.LogUtils;
import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.event.handlers.CommonEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(EntropyLibApi.MOD_ID)
public class EntropyLib {
    public static final Logger LOGGER = LogUtils.getLogger();

    public EntropyLib(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        var commonEventHandler = new CommonEventHandler();

        commonEventHandler.registerModEventHandlers(modEventBus);
        commonEventHandler.registerNeoEventHandlers(NeoForge.EVENT_BUS);
    }
}
