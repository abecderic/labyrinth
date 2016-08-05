package com.abecderic.labyrinth.worldgen.room;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class RoomGenerator
{
    private static RoomGenerator instance;
    private TemplateManager manager;

    private RoomGenerator()
    {
        manager = new TemplateManager("assets/labyrinth/structures/");
    }

    public void generateRoomAt(World world, BlockPos pos, String name)
    {
        Template template = manager.getTemplate(world.getMinecraftServer(), new ResourceLocation("labyrinth:" + name));
        template.addBlocksToWorld(world, pos, new PlacementSettings());
    }

    public static RoomGenerator getInstance()
    {
        if (instance == null)
        {
            instance = new RoomGenerator();
        }
        return instance;
    }
}
