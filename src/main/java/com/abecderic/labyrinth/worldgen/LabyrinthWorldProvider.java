package com.abecderic.labyrinth.worldgen;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.config.Config;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;

public class LabyrinthWorldProvider extends WorldProvider
{
    private LabyrinthChunkGenerator gen;

    public LabyrinthWorldProvider()
    {
        super.hasNoSky = !Config.getConfig().sunlight;
    }

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
        if (gen == null)
        {
            gen = new LabyrinthChunkGenerator(worldObj);
        }
        return gen;
    }
}
