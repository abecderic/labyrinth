package com.abecderic.labyrinth.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;

public class LabyrinthTeleporterOut extends LabyrinthTeleporter
{
    private WorldServer world;

    public LabyrinthTeleporterOut(WorldServer worldIn)
    {
        super(worldIn);
        this.world = worldIn;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw)
    {
        entityIn.setPosition(world.getSpawnPoint().getX() + 0.5D, world.getSpawnPoint().getY() + 0.5D, world.getSpawnPoint().getZ() + 0.5D);
    }
}
