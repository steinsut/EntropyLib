/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib;

import com.mojang.logging.LogUtils;
import me.steinsut.entropylib.api.EntropyLibApi;
import me.steinsut.entropylib.event.handlers.ClientEventHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(value = EntropyLibApi.MOD_ID, dist = Dist.CLIENT)
public class EntropyLibClient {
    public static final Logger LOGGER = LogUtils.getLogger();

    public EntropyLibClient(IEventBus modEventBus, ModContainer container) {
        LOGGER.info("Hello from EntropyLib Client! Overengineering in progress...");

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        ClientEventHandler clientEventHandler = new ClientEventHandler();
        clientEventHandler.registerModEventHandlers(modEventBus);
    }
}
