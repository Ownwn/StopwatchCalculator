package com.ownwn.features;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
public class ToggleCommand {
    public static boolean displayOverlay = false;
    public static long timeDelay = 0;

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("stopwatch").executes(ToggleCommand::toggleDisplay) // placeholder command
        );
    }


    private static int toggleDisplay(CommandContext<FabricClientCommandSource> context) {
        displayOverlay = !displayOverlay;
        context.getSource().sendFeedback(Text.of("§6Toggled the overlay to §b" + displayOverlay + "§6."));

        timeDelay = System.currentTimeMillis();

        return 1; // 1 = success
    }


}
