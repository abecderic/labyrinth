package com.abecderic.labyrinth.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class LabyrinthTeleporter extends Teleporter
{
    private WorldServer world;

    public LabyrinthTeleporter(WorldServer worldIn)
    {
        super(worldIn);
        this.world = worldIn;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw)
    {
        int x = (int) Math.floor(entityIn.getPosition().getX() / 16D);
        int z = (int) Math.floor(entityIn.getPosition().getZ() / 16D);
        x = x * 16 + 8;
        z = z * 16 + 8;
        entityIn.setPositionAndUpdate(x + 0.5D, 66.5D, z + 0.5D);
    }

    @Override
    public boolean makePortal(Entity entityIn)
    {
        return false;
    }

    @Override
    public void removeStalePortalLocations(long worldTime)
    {
        /* NO-OP */
    }
}
