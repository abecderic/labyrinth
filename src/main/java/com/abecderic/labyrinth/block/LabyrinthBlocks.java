package com.abecderic.labyrinth.block;

import com.abecderic.labyrinth.Labyrinth;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class LabyrinthBlocks
{
    public static final String DAEDALUS = "daedalus";
    @GameRegistry.ObjectHolder(Labyrinth.MODID + ":" + LabyrinthBlocks.DAEDALUS)
    public static Block daedalus;

    public static final String PORTAL = "daedalusportal";
    @GameRegistry.ObjectHolder(Labyrinth.MODID + ":" + LabyrinthBlocks.PORTAL)
    public static Block portal;

    public static void registerModels()
    {
        registerModel(daedalus, DAEDALUS);
        registerModel(portal, PORTAL);
    }

    private static void registerModel(Block block, String name)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Labyrinth.MODID + ":" + name, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Labyrinth.MODID + ":" + name, "inventory"));
    }
}
