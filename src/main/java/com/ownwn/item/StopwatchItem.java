package com.ownwn.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.Objects;

public class StopwatchItem extends Item {

    public StopwatchItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        double stuffPerSecond = 0;
        boolean hasUpgrades;
        if (context.getWorld().isClient || context.getHand() != Hand.MAIN_HAND) {
            // cancel client-side context to prevent duplicate calls
            return ActionResult.PASS;
        }

        World world = context.getWorld();
        if (world.getBlockEntity(context.getBlockPos()) == null) {
            // block is not a block entity, so we can ignore it
            return ActionResult.PASS;
        }


        PlayerEntity player = context.getPlayer();
        if (player == null) {
            // player is null can have nasty consequences
            return ActionResult.PASS;
        }

        NbtCompound contents = Objects.requireNonNull(world.getBlockEntity(context.getBlockPos())).createNbtWithId();
        // get nbt data, including the mod ID
        String id = contents.getString("id");
        if (id.equals("")) { // should never happen but check just in case
            return ActionResult.PASS;
        }

        if (id.startsWith("minecraft:")) {
            switch (id) {
                case "minecraft:furnace" -> stuffPerSecond = 0.1;
                case "minecraft:blast_furnace" -> stuffPerSecond = 0.2; // blast furnace is 2x speed
            }
        }

        if (stuffPerSecond == 0) { // block entity was not detected
            sendFail(context);
            return ActionResult.PASS;
        }

        sendSuccess(stuffPerSecond, context);


        return ActionResult.SUCCESS;
    }

    public static void sendSuccess(double machineSpeed, ItemUsageContext context) {
        context.getPlayer().sendMessage(Text.literal("\u00a76This block processes at \u00a7b" + new DecimalFormat("#.###").format(machineSpeed) + " \u00a76things/s"), true);
    } // \u00a7 is a section sign, a partially deprecated way to colour text
    public static void sendFail(ItemUsageContext context) {
        context.getPlayer().sendMessage(Text.literal("\u00a7cUnsupported Machine!"), true);
    }



}
