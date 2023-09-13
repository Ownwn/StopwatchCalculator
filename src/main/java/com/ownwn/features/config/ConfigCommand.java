package com.ownwn.features.config;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ConfigCommand {
    public static long timeDelay = 0;
    /* timeDelay is used to add a delay between when the command is run and when the GUI is opened.
    This is because when running a command, the current screen will be set to null because of the
    chat box being closed.
     */
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                ClientCommandManager.literal("stopwatch").executes(context -> {
                    timeDelay = System.currentTimeMillis();
                    return 0;
                    // return 0 = success
                }));
    }
}
