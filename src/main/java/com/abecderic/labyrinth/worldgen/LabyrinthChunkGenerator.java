package com.abecderic.labyrinth.worldgen;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.config.Config;
import com.abecderic.labyrinth.worldgen.room.RoomGenerator;
import com.abecderic.labyrinth.worldgen.room.RoomInfo;
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
        LabyrinthChunk chunk = Labyrinth.instance.worldData.getDataForChunk(x, z, world.rand);
        String name = Labyrinth.instance.roomLoader.getRoom(chunk.getSize(), world.rand);
        RoomInfo ri = null;
        if (name != null)
        {
            chunk.setName(name);
            ri = Labyrinth.instance.roomLoader.getInfo(name);
        }

        ChunkPrimer primer = new ChunkPrimer();
        int y = 64;
        if (ri != null && ri.down != null && ri.down > 0)
        {
            y -= ri.down;
        }
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                primer.setBlockState(i, y, j, Blocks.BEDROCK.getDefaultState());
            }
        }
        for (int j = 0; j < 7; j++)
        {
            primer.setBlockState(0, 65 + j, 0, Blocks.BEDROCK.getDefaultState());
        }
        for (int i = 1; i < 16; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                if (chunk.getNorth() != LabyrinthChunk.WallType.OPEN && (j == 0 || j > 3 || i < 7 || i > 9 || chunk.getNorth() != LabyrinthChunk.WallType.EXIT))
                {
                    primer.setBlockState(i, 65 + j, 0, Blocks.BEDROCK.getDefaultState());
                }
                if (chunk.getWest() != LabyrinthChunk.WallType.OPEN && (j == 0 || j > 3 || i < 7 || i > 9 || chunk.getWest() != LabyrinthChunk.WallType.EXIT))
                {
                    primer.setBlockState(0, 65 + j, i, Blocks.BEDROCK.getDefaultState());
                }
            }
        }
        int h = 72;
        if (ri != null && ri.up != null && ri.up > 0)
        {
            h += ri.up;
        }
        if (Config.getConfig().generateRoof)
        {
            for (int i = 0; i < 16; i++)
            {
                for (int j = 0; j < 16; j++)
                {
                    primer.setBlockState(i, h, j, Blocks.BEDROCK.getDefaultState());
                }
            }
        }

        Chunk c = new Chunk(world, primer, x, z);
        c.generateSkylightMap();
        return c;
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
        String name = chunk.getName();
        if (name != null && !name.equalsIgnoreCase("null"))
        {
            boolean exitNorth = chunk.getNorth() != LabyrinthChunk.WallType.WALL;
            boolean exitSouth = Labyrinth.instance.worldData.getDataForChunk(x, z + 1, world.rand).getNorth() != LabyrinthChunk.WallType.WALL;
            boolean exitEast = Labyrinth.instance.worldData.getDataForChunk(x + 1, z, world.rand).getWest() != LabyrinthChunk.WallType.WALL;
            boolean exitWest = chunk.getWest() != LabyrinthChunk.WallType.WALL;
            RoomGenerator.getInstance().generateRoomAt(world, x, 65, z, name, chunk.getSize(), exitNorth, exitSouth, exitEast, exitWest);

            RoomInfo ri = Labyrinth.instance.roomLoader.getInfo(name);
            int down = 0;
            if (ri != null && ri.down != null && ri.down > 0)
            {
                down = ri.down;
            }
            int up = 0;
            if (ri != null && ri.up != null && ri.up > 0)
            {
                up = ri.up;
            }
            extendWalls(x, z, up, down);
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
        Biome biome = world.getBiomeGenForCoords(pos);
        return biome.getSpawnableList(creatureType);
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
        /* NO-OP */
    }

    private void extendWalls(int chunkX, int chunkZ, int up, int down)
    {
        if (up > 0)
        {
            extendWallsUp(chunkX, chunkZ, up, true);
            extendWallsUp(chunkX, chunkZ, up, false);
            extendWallsUp(chunkX, chunkZ + 1, up, true);
            extendWallsUp(chunkX + 1, chunkZ, up, false);
        }
        if (down > 0)
        {
            extendWallsDown(chunkX, chunkZ, down, true);
            extendWallsDown(chunkX, chunkZ, down, false);
            extendWallsDown(chunkX, chunkZ + 1, down, true);
            extendWallsDown(chunkX + 1, chunkZ, down, false);
        }
    }

    private void extendWallsUp(int chunkX, int chunkZ, int up, boolean facing)
    {
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < up; j++)
            {
                if (facing)
                    world.setBlockState(new BlockPos(chunkX * 16 + i, 72 + j, chunkZ * 16), Blocks.BEDROCK.getDefaultState());
                else
                    world.setBlockState(new BlockPos(chunkX * 16, 72 + j, chunkZ * 16 + i), Blocks.BEDROCK.getDefaultState());
            }
        }
    }

    private void extendWallsDown(int chunkX, int chunkZ, int down, boolean facing)
    {
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < down; j++)
            {
                if (facing)
                    world.setBlockState(new BlockPos(chunkX * 16 + i, 64 - j, chunkZ * 16), Blocks.BEDROCK.getDefaultState());
                else
                    world.setBlockState(new BlockPos(chunkX * 16, 64 - j, chunkZ * 16 + i), Blocks.BEDROCK.getDefaultState());
            }
        }
    }
}
