package com.ownwn.stopwatchcalculator;

import com.mojang.logging.LogUtils;
import com.ownwn.stopwatchcalculator.block.MekanismBlocks;
import com.ownwn.stopwatchcalculator.item.StopWatchItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(StopwatchCalculator.MODID)
public class StopwatchCalculator
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "stopwatchcalculator";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public StopwatchCalculator()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StopwatchCalculator.MODID);

        RegistryObject<Item> STOPWATCH = ITEMS.register("stopwatch", ()
                -> new StopWatchItem(new Item.Properties().stacksTo(1).durability(-1)));

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Test());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MekanismBlocks.populateMachineTiers(MekanismBlocks.MEKANISM_MACHINE_TIERS);
        MekanismBlocks.populateMilibucketMachines(MekanismBlocks.MEKANISM_MILLIBUCKET_MACHINES);
        MekanismBlocks.populateItemMachines(MekanismBlocks.MEKANISM_ITEM_MACHINES);
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {}
    }
}
