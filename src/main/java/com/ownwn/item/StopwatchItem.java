package com.ownwn.item;

import com.ownwn.features.AdAstraBlocks;
import com.ownwn.features.MekanismBlocks;
import com.ownwn.features.config.ConfigOption;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Objects;

public class StopwatchItem extends Item {

    public static final int COMPOUND_LIST_KEY = 10;  // 10 indicates a list of CompoundTag elements

    public StopwatchItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        double processingSpeed = 0;
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
        if (ConfigOption.debugMode.getValue()) {
            System.out.println(contents);
        }

        // get nbt data, including the mod ID
        String id = contents.getString("id");
        if (id.equals("")) { // should never happen but check just in case
            return ActionResult.PASS;
        }

        if (id.startsWith("minecraft:")) {
            switch (id) {
                case "minecraft:furnace" -> processingSpeed = 0.1;
                case "minecraft:blast_furnace" -> processingSpeed = 0.2; // blast furnace is 2x speed
            }
            if (ConfigOption.debugMode.getValue()) {
                System.out.println(id);
            }

        } else if (id.startsWith("mekanism")) {

            boolean foundMachineType = false;
            for (Map.Entry<String, Double> machineID : MekanismBlocks.MEKANISM_ITEM_MACHINES.entrySet()) {

                if (ConfigOption.debugMode.getValue()) {
                    System.out.println(machineID.getKey());
                    System.out.println(machineID.getValue());
                }

                if (id.endsWith(machineID.getKey())) {
                    foundMachineType = true;
                    processingSpeed = machineID.getValue();
                    for (Map.Entry<String, Integer> machineTier : MekanismBlocks.MEKANISM_MACHINE_TIERS.entrySet()) {
                        String key = machineTier.getKey();

                        if (id.replace("mekanism:", "").startsWith(key)) {
                            processingSpeed *= machineTier.getValue(); // multiply the speed of the machine by it's tier
                            break;
                        }
                    }
                    break;
                }
            }
            if (!foundMachineType) {
                for (Map.Entry<String, Double> entry : MekanismBlocks.MEKANISM_MILLIBUCKET_MACHINES.entrySet()) {
                    if (id.endsWith(entry.getKey())) {
                        foundMachineType = true;
                        processingSpeed = entry.getValue();
                        break;
                    }
                }
            }

            NbtCompound upgradeTag = contents.getCompound("componentUpgrade").getCompound("upgrades");

            hasUpgrades = !upgradeTag.toString().equals("");

            /* mekanism upgrades
             *  0: speed
             *  1: energy
             *  2: filter
             *  3: gas
             *  4: muffling
             *  5: anchor
             *  6: stone generator
             * */

            if (hasUpgrades) {
                NbtList upgradesListTag = contents.getCompound("componentUpgrade").getList("upgrades", COMPOUND_LIST_KEY);
                for (int i = 0; i < upgradesListTag.size(); i++) {
                    NbtCompound upgradeTypeAmountTag = upgradesListTag.getCompound(i);
                    int upgradeType = upgradeTypeAmountTag.getInt("type");
                    int upgradeAmount = upgradeTypeAmountTag.getInt("amount");

                    if (ConfigOption.debugMode.getValue()) {
                        System.out.println("value of type: " + String.valueOf(foundMachineType));
                        System.out.println("value of amount: " + String.valueOf(upgradeAmount));
                    }

                    if (upgradeType == 0) { // speed upgrade
                        boolean isStrong = false;
                        for (String machineID: MekanismBlocks.STRONG_UPGRADE_MACHINES) {
                            if (id.endsWith(machineID)) {
                                processingSpeed *= MekanismBlocks.FAST_SPEED_UPGRADES[upgradeAmount];
                                isStrong = true; // strong_upgrade_machines gain a higher speed multiplier from speed upgrades than normal
                                break;
                            }
                        }
                        if (!isStrong) {
                            processingSpeed *= MekanismBlocks.SLOW_SPEED_UPGRADES[upgradeAmount];
                        }
                    }
                }
            }

        } else if (id.startsWith("ad_astra:")) { // burn time: 200 per 10 seconds == 1 per tick
            String name = id.replace("ad_astra:", "");
            if (ConfigOption.debugMode.getValue()) {
                System.out.println("Machine Name: " + name);
            }
            if (name.equals("coal_generator")) {
                if (ConfigOption.debugMode.getValue()) {
                    System.out.println("NBT Contents: " + contents);
                }
                double cookTimeSeconds = contents.getInt("CookTimeTotal") / 20.0;
                if (cookTimeSeconds == 0) {
                    sendFail(context, false);
                    return ActionResult.PASS;
                }
                processingSpeed = 1.0 / (cookTimeSeconds); // speed is the inverse of the total cook time of one item
            } else {
                for (Map.Entry<String, Double> machineSet : AdAstraBlocks.AD_ASTRA_MACHINES.entrySet()) {
                    if (name.equals(machineSet.getKey())) {
                        processingSpeed = machineSet.getValue();
                        break;
                    }
                }
            }
            if (ConfigOption.debugMode.getValue()) {
                System.out.println("Processing Speed: " + processingSpeed);
            }
        }

        if (processingSpeed == 0) { // block entity was not detected
            sendFail(context, true);
            return ActionResult.PASS;
        }

        sendSuccess(processingSpeed, context);
        return ActionResult.SUCCESS;
    }

    public static void sendSuccess(double machineSpeed, ItemUsageContext context) {
        if (ConfigOption.actionBarDisplay.getValue()) {
            DrawHelper.drawSuccess(machineSpeed);
            return;
        }
        String text = ConfigOption.shortMessages.getValue() ? "\u00a76Speed: \u00a7b" + new DecimalFormat("#.###").format(machineSpeed) + "\u00a76/s" : "\u00a76This block processes at \u00a7b" + new DecimalFormat("#.###").format(machineSpeed) + " \u00a76things/s";
        context.getPlayer().sendMessage(Text.literal(text));

    } // \u00a7 is a section sign, an easier but partially deprecated way to colour text
    public static void sendFail(ItemUsageContext context, boolean unsupported) { // unsupported = false if an error occured
        if (ConfigOption.actionBarDisplay.getValue()) {
            DrawHelper.drawFail(unsupported);
            return;
        }
        String text = unsupported ? "\u00a7cUnsupported Machine!" : "\u00a7cAn error occured!";
        context.getPlayer().sendMessage(Text.literal(text));

    }



}
