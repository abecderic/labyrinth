package com.abecderic.labyrinth.block;

import com.abecderic.labyrinth.Labyrinth;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class LabyrinthBlocks
{
    public static final String DAEDALUS = "daedalus";
    public static Block daedalus;

    public static void registerBlocks()
    {
        daedalus = new BlockDaedalus();
        GameRegistry.register(daedalus);
        GameRegistry.register(new ItemBlock(daedalus).setRegistryName(daedalus.getRegistryName()));
    }

    public static void registerModels()
    {
        registerModel(daedalus, DAEDALUS);
    }

    private static void registerModel(Block block, String name)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Labyrinth.MODID + ":" + name, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Labyrinth.MODID + ":" + name, "inventory"));
    }
}
