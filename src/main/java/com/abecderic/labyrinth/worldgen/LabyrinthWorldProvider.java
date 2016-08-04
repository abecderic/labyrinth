package com.abecderic.labyrinth.worldgen;

import com.abecderic.labyrinth.Labyrinth;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;

public class LabyrinthWorldProvider extends WorldProvider
{
    @Override
    public DimensionType getDimensionType()
    {
        return Labyrinth.instance.dimensionType;
    }

    @Override
    public boolean isSurfaceWorld()
    {
        return false;
    }

    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return new LabyrinthChunkGenerator(worldObj);
    }
}
