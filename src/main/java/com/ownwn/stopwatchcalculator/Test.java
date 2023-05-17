package com.ownwn.stopwatchcalculator;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

public class Test {
    public static long displayTime = 0;

    public static boolean isOn = false;
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGuiOverlayEvent event) {
        if (!isOn) {
            return;
        }
        if (Minecraft.getInstance().player == null) {
            return;
        }
        if (displayTime == 0) {
            return;
        }
        if (System.currentTimeMillis() > displayTime) {
            return;
        }



//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//        RenderSystem.setShaderTexture(0, loc);
        int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();



        // Get the RGB value of the Color object as a decimal value


        float hue = (System.currentTimeMillis() % 10000) / 10000.0f; // calculate hue value based on current time
        float saturation = 1.0f; // full saturation
        float brightness = 1.0f; // full brightness
        Color color = Color.getHSBColor(hue, saturation, brightness); // create a color with the calculated hue value
        int decimalColor = color.getRGB();



        PoseStack poseStack = new PoseStack();
//        poseStack.translate(xTranslate, 0, 0);

        GuiComponent.fill(poseStack, 5, 5, width - 5, 15, decimalColor); //top
        GuiComponent.fill(poseStack, 5, height -5, width -5, height -15, decimalColor); // bottom

        GuiComponent.fill(poseStack, 5, 15, 15, height -15, decimalColor); // left
        GuiComponent.fill(poseStack, width - 5, 15, width -15, height -15, decimalColor); // right
        

//        GL11.glMatrixMode(GL11.GL_PROJECTION);
//        GL11.glLoadIdentity();
//        GL11.glOrtho(0, screenWidth, screenHeight, 0, 1000, 3000);
//        GL11.glMatrixMode(GL11.GL_MODELVIEW);
//        GL11.glLoadIdentity();
//        GL11.glTranslated(screenWidth / 2, screenHeight / 2, 0);
//
//        GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F); // set the color of your shape
//
//        GL11.glBegin(GL11.GL_QUADS); // draw a square
//        GL11.glVertex2f(-50.0F, -50.0F);
//        GL11.glVertex2f(50.0F, -50.0F);
//        GL11.glVertex2f(50.0F, 50.0F);
//        GL11.glVertex2f(-50.0F, 50.0F);
//        GL11.glEnd();
//
//        GL11.glPopMatrix();


//        Minecraft.getInstance().player.sendSystemMessage(Component.nullToEmpty("aa"));
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        if (!event.getMessage().contains(".toggle")) {
            return;
        }
        isOn = !isOn;
                Minecraft.getInstance().player. sendSystemMessage(Component.nullToEmpty(String.valueOf(isOn)));


    }
}
