package com.abecderic.labyrinth.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class LabyrinthTeleporter extends Teleporter
{
    public LabyrinthTeleporter(WorldServer worldIn)
    {
        super(worldIn);
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw)
    {
        /* NO-OP */
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
