package com.ownwn.features;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ToggleCommand {
    public static long timeDelay = 0;
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("stopwatch").executes(context -> {
                    timeDelay = System.currentTimeMillis();
                    return 0;
                    // return 0 = success
                }));
    }
}
