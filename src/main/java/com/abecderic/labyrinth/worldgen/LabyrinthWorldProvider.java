package com.abecderic.labyrinth.worldgen;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;

import java.util.Random;

public class LabyrinthWorldProvider extends WorldProvider
{
    private Random r = new Random();
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

    @Override
    public BlockPos getRandomizedSpawnPoint()
    {
        BlockPos spawn = super.getRandomizedSpawnPoint();
        int x = (int)Math.floor(spawn.getX() / 16D + r.nextGaussian() * 8);
        int z = (int)Math.floor(spawn.getZ() / 16D + r.nextGaussian() * 8);
        x = x * 16 + 8;
        z = z * 16 + 8;
        return new BlockPos(x, 66, z);
    }
}
