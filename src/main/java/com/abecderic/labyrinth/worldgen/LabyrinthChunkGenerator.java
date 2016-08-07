package com.abecderic.labyrinth.worldgen;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.config.Config;
import com.abecderic.labyrinth.worldgen.room.RoomGenerator;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
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
        ChunkPrimer primer = new ChunkPrimer();
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                primer.setBlockState(i, 64, j, Blocks.BEDROCK.getDefaultState());
            }
        }
        LabyrinthChunk data = Labyrinth.instance.worldData.getDataForChunk(x, z, world.rand);
        for (int j = 0; j < 7; j++)
        {
            primer.setBlockState(0, 65 + j, 0, Blocks.BEDROCK.getDefaultState());
        }
        for (int i = 1; i < 16; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if (data.getNorth() != LabyrinthChunk.WallType.OPEN && (j == 0 || j > 3 || i < 7 || i > 9 || data.getNorth() != LabyrinthChunk.WallType.EXIT))
                {
                    primer.setBlockState(i, 65 + j, 0, Blocks.BEDROCK.getDefaultState());
                }
                if (data.getWest() != LabyrinthChunk.WallType.OPEN && (j == 0 || j > 3 || i < 7 || i > 9 || data.getWest() != LabyrinthChunk.WallType.EXIT))
                {
                    primer.setBlockState(0, 65 + j, i, Blocks.BEDROCK.getDefaultState());
                }
            }
        }
        if (Config.getConfig().generateRoof)
        {
            for (int i = 0; i < 16; i++)
            {
                for (int j = 0; j < 16; j++)
                {
                    primer.setBlockState(i, 71, j, Blocks.BEDROCK.getDefaultState());
                }
            }
        }
        Chunk chunk = new Chunk(world, primer, x, z);
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z)
    {
        LabyrinthChunk chunk = Labyrinth.instance.worldData.getDataForChunk(x, z, world.rand);
        if (chunk.getSize() != LabyrinthChunk.Size.SINGLE)
        {
            if (chunk.getNorth() == LabyrinthChunk.WallType.OPEN) return;
            if (chunk.getWest() == LabyrinthChunk.WallType.OPEN) return;
        }
        String name = Labyrinth.instance.roomLoader.getRoom(chunk.getSize(), world.rand);
        if (name != null)
        {
            boolean exitNorth = chunk.getNorth() != LabyrinthChunk.WallType.WALL;
            boolean exitSouth = Labyrinth.instance.worldData.getDataForChunk(x, z + 1, world.rand).getNorth() != LabyrinthChunk.WallType.WALL;
            boolean exitEast = Labyrinth.instance.worldData.getDataForChunk(x + 1, z, world.rand).getWest() != LabyrinthChunk.WallType.WALL;
            boolean exitWest = chunk.getWest() != LabyrinthChunk.WallType.WALL;
            RoomGenerator.getInstance().generateRoomAt(world, x, 65, z, name, chunk.getSize(), exitNorth, exitSouth, exitEast, exitWest);
            chunk.setName(name);
        }
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
