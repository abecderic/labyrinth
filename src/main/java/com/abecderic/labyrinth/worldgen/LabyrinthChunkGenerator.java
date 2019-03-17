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
import net.minecraft.world.gen.IChunkGenerator;

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
    public Chunk generateChunk(int x, int z)
    {
        if (Labyrinth.instance.worldData == null)
            Labyrinth.instance.loadWorldData(world.getMinecraftServer());
        LabyrinthChunk chunk = Labyrinth.instance.worldData.getDataForChunk(x, z, world.rand);
        String name = chunk.getName();
        if (name == null || name.equalsIgnoreCase("null"))
        {
            name = Labyrinth.instance.roomLoader.getRoom(chunk.getSize(), world.rand);
        }
        RoomInfo ri = null;
        if (name != null)
        {
            chunk.setName(name);
            ri = Labyrinth.instance.roomLoader.getInfo(name);
        }

        ChunkPrimer primer = new ChunkPrimer();

        /* floor */
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

        /* corner pillar */
        if (chunk.getNorth() != LabyrinthChunk.WallType.OPEN || chunk.getWest() != LabyrinthChunk.WallType.OPEN)
        {
            for (int j = 0; j < 7; j++)
            {
                primer.setBlockState(0, 65 + j, 0, Blocks.BEDROCK.getDefaultState());
            }
        }

        /* north and west walls */
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

        /* roof */
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

        /* wall extension */
        LabyrinthChunk chunkNorth = Labyrinth.instance.worldData.getDataForChunk(x, z - 1, world.rand);
        String nameNorth = chunkNorth.getName();
        if (nameNorth == null || nameNorth.equalsIgnoreCase("null"))
        {
            nameNorth = Labyrinth.instance.roomLoader.getRoom(chunkNorth.getSize(), world.rand);
            if (nameNorth != null)
            {
                chunkNorth.setName(nameNorth);
                Labyrinth.instance.worldData.markDirty();
            }
            else
            {
                Labyrinth.instance.logger.error("got null for nameNorth for size " + chunkNorth.getSize());
            }
        }
        RoomInfo riNorth = null;
        if (nameNorth != null)
        {
            riNorth = Labyrinth.instance.roomLoader.getInfo(nameNorth);
        }

        LabyrinthChunk chunkWest = Labyrinth.instance.worldData.getDataForChunk(x - 1, z, world.rand);
        String nameWest = chunkWest.getName();
        if (nameWest == null || nameWest.equalsIgnoreCase("null"))
        {
            nameWest = Labyrinth.instance.roomLoader.getRoom(chunkWest.getSize(), world.rand);
            if (nameWest != null)
            {
                chunkWest.setName(nameWest);
                Labyrinth.instance.worldData.markDirty();
            }
            else
            {
                Labyrinth.instance.logger.error("got null for nameWest for size " + chunkWest.getSize());
            }
        }
        RoomInfo riWest = null;
        if (nameWest != null)
        {
            riWest = Labyrinth.instance.roomLoader.getInfo(nameWest);
        }

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
        /* north */
        if (chunk.getNorth() != LabyrinthChunk.WallType.OPEN)
        {
            int northDown = down;
            if (riNorth != null && riNorth.down != null && riNorth.down > 0)
            {
                northDown = Math.max(northDown, riNorth.down);
            }
            int northUp = up;
            if (riNorth != null && riNorth.up != null && riNorth.up > 0)
            {
                northUp = Math.max(northUp, riNorth.up);
            }
            extendWallsUp(primer, northUp + 1, true);
            extendWallsDown(primer, northDown + 1, true);
        }
        /* west */
        if (chunk.getWest() != LabyrinthChunk.WallType.OPEN)
        {
            int westDown = down;
            if (riWest != null && riWest.down != null && riWest.down > 0)
            {
                westDown = Math.max(westDown, riWest.down);
            }
            int westUp = up;
            if (riWest != null && riWest.up != null && riWest.up > 0)
            {
                westUp = Math.max(westUp, riWest.up);
            }
            extendWallsUp(primer, westUp + 1, false);
            extendWallsDown(primer, westDown + 1, false);
        }

        Chunk c = new Chunk(world, primer, x, z);
        c.generateSkylightMap();
        return c;
    }

    @Override
    public void populate(int x, int z)
    {
        LabyrinthChunk chunk = Labyrinth.instance.worldData.getDataForChunk(x, z, world.rand);
        boolean spawnRoom = true;
        if (chunk.getSize() != LabyrinthChunk.Size.SINGLE)
        {
            if (chunk.getNorth() == LabyrinthChunk.WallType.OPEN) spawnRoom = false;
            if (chunk.getWest() == LabyrinthChunk.WallType.OPEN) spawnRoom = false;
        }
        String name = chunk.getName();
        if (name != null && !name.equalsIgnoreCase("null"))
        {
            if (spawnRoom)
            {
                boolean exitNorth = chunk.getNorth() != LabyrinthChunk.WallType.WALL;
                boolean exitSouth = Labyrinth.instance.worldData.getDataForChunk(x, z + 1, world.rand).getNorth() != LabyrinthChunk.WallType.WALL;
                boolean exitEast = Labyrinth.instance.worldData.getDataForChunk(x + 1, z, world.rand).getWest() != LabyrinthChunk.WallType.WALL;
                boolean exitWest = chunk.getWest() != LabyrinthChunk.WallType.WALL;
                RoomGenerator.getInstance().generateRoomAt(world, x, 65, z, name, chunk.getSize(), exitNorth, exitSouth, exitEast, exitWest);
            }
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
        Biome biome = world.getBiome(pos);
        return biome.getSpawnableList(creatureType);
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
        /* NO-OP */
    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        return false;
    }

    private void extendWallsUp(ChunkPrimer primer, int up, boolean facing)
    {
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < up; j++)
            {
                if (facing)
                    primer.setBlockState(i, 72 + j, 0, Blocks.BEDROCK.getDefaultState());
                else
                    primer.setBlockState(0, 72 + j, i, Blocks.BEDROCK.getDefaultState());
            }
        }
    }

    private void extendWallsDown(ChunkPrimer primer, int down, boolean facing)
    {
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < down; j++)
            {
                if (facing)
                    primer.setBlockState(i, 64 - j, 0, Blocks.BEDROCK.getDefaultState());
                else
                    primer.setBlockState(0, 64 - j, i, Blocks.BEDROCK.getDefaultState());
            }
        }
    }
}
