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
import me.steinsut.entropylib.api.dyn.entity.sync.Policies;
import me.steinsut.entropylib.event.handlers.CommonEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(EntropyLibApi.MOD_ID)
public class EntropyLib {
    public static final Logger LOGGER = LogUtils.getLogger();

    private Class<?>[] classes = new Class[]{
            Policies.class
    };

    /**
     * Java has the peculiar behavior of lazy loading classes. While this may sound great, it has the obvious drawback
     * of not loading classes that are never accessed (see me.steinsut.entropylib.api.dyn.entity.sync.Policies)
     *
     * This is to work around that, because the other options are:
     * - Access some public static member of the class somewhere without it being optimized away (lol no)
     * - Put an empty public static method in the class and call it somewhere (lol no)
     *
     * Thank you Java for forcing my hand into doing stupid shit like this, I can't wait for the next scuffed implementation
     * of a new feature
     */
    private void forceLoadClasses() {
        for (Class<?> clazz : classes) {
            try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            }
            catch (ClassNotFoundException _) {}
        }
    }

    public EntropyLib(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Hello from EntropyLib! Overengineering in progress...");
        this.forceLoadClasses();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        CommonEventHandler commonEventHandler = new CommonEventHandler();

        commonEventHandler.registerModEventHandlers(modEventBus);
        commonEventHandler.registerNeoEventHandlers(NeoForge.EVENT_BUS);
    }
}
