package com.abecderic.labyrinth.worldgen;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;

public class LabyrinthChunkGenerator implements IChunkGenerator
{
    private World world;

    public LabyrinthChunkGenerator(World world)
    {
        this.world = world;
    }

    @Override
    public Chunk provideChunk(int x, int z)
    {
        Chunk c = new Chunk(world, x, z);
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                c.setBlockState(new BlockPos(i, 64, j), Blocks.BEDROCK.getDefaultState());
            }
        }
        for (int i = 0; i < 16; i++)
        {
            if (z < 0) c.setBlockState(new BlockPos(i, 65, 0), Blocks.CLAY.getDefaultState());
            else c.setBlockState(new BlockPos(i, 65, 16), Blocks.WOOL.getDefaultState());
            if (z < 0) c.setBlockState(new BlockPos(16, 65, i), Blocks.CLAY.getDefaultState());
            else c.setBlockState(new BlockPos(0, 65, i), Blocks.WOOL.getDefaultState());
        }
        return c;
    }

    @Override
    public void populate(int x, int z)
    {

    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return null;
    }

    @Nullable
    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
    {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {

    }
}
