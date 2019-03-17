package com.abecderic.labyrinth.proxy;

import com.abecderic.labyrinth.block.BlockDaedalus;
import com.abecderic.labyrinth.block.BlockDaedalusPortal;
import com.abecderic.labyrinth.block.LabyrinthBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonProxy
{
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(new BlockDaedalus());
        event.getRegistry().register(new BlockDaedalusPortal());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new ItemBlock(LabyrinthBlocks.daedalus).setRegistryName(LabyrinthBlocks.daedalus.getRegistryName()));
    }

    public void registerModels()
    {
        /* NO-OP */
    }
}
