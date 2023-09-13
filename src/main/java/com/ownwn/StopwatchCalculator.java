package com.ownwn;

import com.ownwn.features.AdAstraBlocks;
import com.ownwn.features.config.ConfigOption;
import com.ownwn.features.config.ConfigCommand;
import com.ownwn.item.StopwatchItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopwatchCalculator implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("stopwatchcalculator");
    public static final String MODID = "stopwatchcalculator";

    private static final KeyBinding configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Open Config", // name
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B, // "B" key
            "Stopwatch Calculator" // category
    ));

    @Override
    public void onInitialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> ConfigCommand.register(dispatcher) );

        Item STOPWATCH = new StopwatchItem(new Item.Settings().maxCount(1));
        ConfigOption.registerConfig();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(STOPWATCH));
        Registry.register(Registries.ITEM, new Identifier(MODID, "stopwatch"), STOPWATCH);
        AdAstraBlocks.populateAstraMachines(); // add the ad astra machines to be used for stopwatch
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKey.wasPressed()) {
                ConfigOption.openConfig();
            }

            if (System.currentTimeMillis() - ConfigCommand.timeDelay > 50) {
                // add a delay of 50ms before opening the gui to stop it instantly closing when the chat handler tries to close the chat window
                return;
            }
            ConfigOption.openConfig();
            ConfigCommand.timeDelay = 0;
        });







    }
}