package com.ownwn.stopwatchcalculator.item;

import com.ownwn.stopwatchcalculator.Test;
import com.ownwn.stopwatchcalculator.block.MekanismBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Objects;

public class StopWatchItem extends Item {

    public StopWatchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        double stuffPerSecond = 0;
        boolean hasUpgrades;
        if (context.getLevel().isClientSide() || context.getHand() != InteractionHand.MAIN_HAND) {
            return super.useOn(context);
        }
        Level level = context.getLevel();
        if (level.getBlockEntity(context.getClickedPos()) == null) { //  || !level.getBlockState(context.getClickedPos()).is(Blocks.CHEST)
            return super.useOn(context);
        }
        CompoundTag contents = Objects.requireNonNull(level.getBlockEntity(context.getClickedPos())).serializeNBT();
        Player player = context.getPlayer();
        if (player == null) {
            return super.useOn(context);
        }
//        Msg(contents.toString(), player);
        String id = contents.getString("id");
        if (id.equals("")) {
            return super.useOn(context);
        }

        if (id.startsWith("minecraft:")) {
            switch (id) {
                case "minecraft:furnace" -> stuffPerSecond = 0.1;
                case "minecraft:blast_furnace" -> stuffPerSecond = 0.2;
            }




        } else if (id.startsWith("mekanism")) {
            boolean foundMachineType = false;
            for (Map.Entry<String, Double> machineID : MekanismBlocks.MEKANISM_ITEM_MACHINES.entrySet()) {
                if (id.endsWith(machineID.getKey())) {
                    foundMachineType = true;
                    stuffPerSecond = machineID.getValue();
                    for (Map.Entry<String, Integer> machineTier : MekanismBlocks.MEKANISM_MACHINE_TIERS.entrySet()) {
                        String key = machineTier.getKey();
//                        Msg(key, player);
                        if (id.replace("mekanism:", "").startsWith(key)) {
//                            Msg(String.valueOf(stuffPerSecond), player);
                            stuffPerSecond *= machineTier.getValue();
//                            Msg(String.valueOf(stuffPerSecond), player);
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
                        stuffPerSecond = entry.getValue();
                        break;
                    }
                }
            }

            Tag upgradeTag = contents.getCompound("componentUpgrade").getCompound("upgrades");
//            Msg(String.valueOf(upgradeTag), player);
//
//            Msg(String.valueOf(contents.getCompound("componentUpgrade").get("upgrades")), player);
//            Msg(String.valueOf(upgradeTag), player);
//            Msg(String.valueOf(contents.getCompound("componentUpgrade")), player);
            hasUpgrades = !upgradeTag.toString().equals("");
//            Msg(String.valueOf(hasUpgrades), player);

            /* 0: speed
            *  1: energy
            *  2: filter
            *  3: gas
            *  4: muffling
            *  5: anchor
            *  6: stone generator
            * */

            if (hasUpgrades) {
                ListTag upgradesListTag = contents.getCompound("componentUpgrade").getList("upgrades", 10); // 10 indicates a list of CompoundTag elements
                for (int i = 0; i < upgradesListTag.size(); i++) {
                    CompoundTag upgradeTypeAmountTag = upgradesListTag.getCompound(i);
                    int type = upgradeTypeAmountTag.getInt("type");
                    int amount = upgradeTypeAmountTag.getInt("amount");
//                    Msg(String.valueOf(type), player);
//                    Msg(String.valueOf(amount), player);
                    if (type == 0) {
                        boolean isStrong = false;
                        for (String machineID: MekanismBlocks.STRONG_UPGRADE_MACHINES) {
                            if (id.endsWith(machineID)) {
                                stuffPerSecond *= MekanismBlocks.FAST_SPEED_UPGRADES[amount];
                                isStrong = true;
                                break;
                            }
                        }
                        if (!isStrong) {
                            stuffPerSecond *= MekanismBlocks.SLOW_SPEED_UPGRADES[amount];
                        }
                    }
                }
            }

        }
        if (stuffPerSecond == 0) {
            Msg("Unsupported Machine!", player);
        } else {
            Msg("This block processes at " + new DecimalFormat("#.###").format(stuffPerSecond) + " things/s", player);
        }
        Test.displayTime = System.currentTimeMillis() + 1000;
        return super.useOn(context);
    }

    private void Msg(String message, Player player) {

        player.displayClientMessage(Component.nullToEmpty(message), true);

    }
}
