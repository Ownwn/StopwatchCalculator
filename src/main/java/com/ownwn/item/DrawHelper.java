package com.ownwn.item;

import com.ownwn.features.config.ConfigOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.text.DecimalFormat;

public class DrawHelper {
    public static long drawTime = 0; // used to draw for a specified period of time
    public static String message = "";
    public static void doDraw(MatrixStack matrices) {
        if (MinecraftClient.getInstance().player == null) {
            return;
        }
        if (drawTime == 0 || message.equals("")) {
            return;
        }
        if (System.currentTimeMillis() > drawTime) {
            return;
        }

        float upScale = 1.6f;
        float downScale = 1f / 1.6f;

        matrices.scale(upScale, upScale, upScale); // scale down to draw
        float x = (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f) - ((MinecraftClient.getInstance().textRenderer.getWidth(message) / 2f) * upScale);
        float y = MinecraftClient.getInstance().getWindow().getScaledHeight() - 80;


        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, message, x * 0.625f, y * 0.625f, 16777215);
        // offset x and y values to ensure it's drawn in the right place

        matrices.scale(downScale, downScale, downScale); // scale back up to continue drawing
    }

    public static void drawSuccess(double machineSpeed) {
        String processType = message.isEmpty() ? "items" : "millibuckets";
        message = ConfigOption.shortMessages.getValue() ? "\u00a76Speed: \u00a7b" + new DecimalFormat("#.###").format(machineSpeed) + "\u00a76/s" : "\u00a76This block processes at \u00a7b" + new DecimalFormat("#.###").format(machineSpeed) + " \u00a76" + processType + "/s";
        drawTime = System.currentTimeMillis() + 2000;
        //u00a7 followed by a character is used to colour text.
    }

    public static void drawFail(boolean unsupported) {
        message = unsupported ? "\u00a7cUnsupported Machine!" : "\u00a7cAn error occured!";
        drawTime = System.currentTimeMillis() + 2000;
    }
}
