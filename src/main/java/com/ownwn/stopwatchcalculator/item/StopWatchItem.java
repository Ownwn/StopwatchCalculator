package com.ownwn.stopwatchcalculator.item;

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

import java.util.Objects;

public class StopWatchItem extends Item {
    public static boolean hasUpgrades = false;
    public static double stuffPerSecond = 0;

    public StopWatchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
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
            for (String machineID: MekanismBlocks.tenSecondTimes) {
                if (id.endsWith(machineID)) {
                    stuffPerSecond = 0.1;
                    break;
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
                        Msg("Aaaaa" + MekanismBlocks.speedUpgrades[amount], player);
                        stuffPerSecond = stuffPerSecond * MekanismBlocks.speedUpgrades[amount];
                    }
                }
            }

        }
        Msg("This block processes at " + stuffPerSecond + " things/s", player);
        return super.useOn(context);
    }

    private void Msg(String message, Player player) {
        player.sendSystemMessage(Component.nullToEmpty(message));
    }
}
