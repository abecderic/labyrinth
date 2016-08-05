package com.abecderic.labyrinth.worldgen.algorithm;

import com.abecderic.labyrinth.worldgen.LabyrinthChunk;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LabyrinthGenerator
{
    private static final int EDGE_EXIT_CHANCE = 4;
    private static final int SPECIAL_ROOMS_ITERATIONS = 32;
    private static final int X = 16;
    private static final int Z = 16;
    private Cell[][] cells;
    private List<EnumFacing> directions = new ArrayList<EnumFacing>();
    private static LabyrinthGenerator instance;

    public LabyrinthGenerator()
    {
        directions.add(EnumFacing.NORTH);
        directions.add(EnumFacing.EAST);
        directions.add(EnumFacing.SOUTH);
        directions.add(EnumFacing.WEST);
    }

    public LabyrinthChunk[][] createLabyrinth(Random random)
    {
        cells = new Cell[X][Z];
        for (int i = 0; i < X; i++)
        {
            for (int j = 0; j < Z; j++)
            {
                cells[i][j] = new Cell();
            }
        }
        createLabyrinth(0, 0);
        LabyrinthChunk[][] chunks = new LabyrinthChunk[X][Z];
        for (int i = 0; i < X; i++)
        {
            for (int j = 0; j < Z; j++)
            {
                if (i == 0)
                {
                    chunks[i][j] = new LabyrinthChunk(cells[i][j].hasExit(EnumFacing.NORTH.getIndex() - 2), random.nextInt(EDGE_EXIT_CHANCE) == 0);
                }
                else if (j == 0)
                {
                    chunks[i][j] = new LabyrinthChunk(random.nextInt(EDGE_EXIT_CHANCE) == 0, cells[i][j].hasExit(EnumFacing.WEST.getIndex() - 2));
                }
                else
                {
                    chunks[i][j] = new LabyrinthChunk(cells[i][j].hasExit(EnumFacing.NORTH.getIndex() - 2), cells[i][j].hasExit(EnumFacing.WEST.getIndex() - 2));
                }
            }
        }
        for (int i = 0; i < SPECIAL_ROOMS_ITERATIONS; i++)
        {
            int x = random.nextInt(X);
            int z = random.nextInt(Z);
            if (chunks[x][z].getSize() == LabyrinthChunk.Size.SINGLE)
            {
                List<LabyrinthChunk> north = getSpecialRoomTo(x, z, cells, chunks, EnumFacing.NORTH);
                List<LabyrinthChunk> south = getSpecialRoomTo(x, z, cells, chunks, EnumFacing.SOUTH);
                List<LabyrinthChunk> east = getSpecialRoomTo(x, z, cells, chunks, EnumFacing.EAST);
                List<LabyrinthChunk> west = getSpecialRoomTo(x, z, cells, chunks, EnumFacing.WEST);
                List<LabyrinthChunk> largest = getLargestList(north, south, east, west);
                if (largest == north || largest == south)
                {
                    fillList(largest, false);
                }
                else
                {
                    fillList(largest, true);
                }
            }
        }
        return chunks;
    }

    private void createLabyrinth(int currentX, int currentZ)
    {
        Collections.shuffle(directions);
        EnumFacing[] dirs = directions.toArray(new EnumFacing[directions.size()]);
        for (EnumFacing direction : dirs)
        {
            int newX = currentX + direction.getFrontOffsetX();
            int newZ = currentZ + direction.getFrontOffsetZ();
            if (newX >= 0 && newX < X && newZ >= 0 && newZ < Z)
            {
                if (!cells[newX][newZ].hasExits())
                {
                    cells[currentX][currentZ].addExit(direction.getIndex() - 2);
                    cells[newX][newZ].addExit(direction.getOpposite().getIndex() - 2);
                    createLabyrinth(newX, newZ);
                }
            }
        }
    }

    private List<LabyrinthChunk> getSpecialRoomTo(int x, int z, Cell[][] cells, LabyrinthChunk[][] chunks, EnumFacing dir)
    {
        int newX = x + dir.getFrontOffsetX();
        int newZ = z + dir.getFrontOffsetZ();
        if (newX >= 0 && newX < X && newZ >= 0 && newZ < Z)
        {
            if (cells[x][z].hasExit(dir.getIndex() - 2) && chunks[newX][newZ].getSize() == LabyrinthChunk.Size.SINGLE)
            {
                List<LabyrinthChunk> room = getSpecialRoomTo(newX, newZ, cells, chunks, dir);
                room.add(chunks[x][z]);
                return room;
            }
        }
        return new ArrayList<LabyrinthChunk>();
    }

    private List<LabyrinthChunk> getLargestList(List<LabyrinthChunk>... lists)
    {
        List<LabyrinthChunk> largest = new ArrayList<LabyrinthChunk>();
        for (List<LabyrinthChunk> list : lists)
        {
            if (list.size() > largest.size())
                largest = list;
        }
        return largest;
    }

    private void fillList(List<LabyrinthChunk> list, boolean xAxis)
    {
        if (list.size() > 4)
        {
            list = list.subList(0, 4);
        }
        if (list.size() > 1)
        {
            for (LabyrinthChunk chunk : list)
            {
                chunk.setSize(LabyrinthChunk.Size.values()[list.size() - 1 + (xAxis ? 0 : 4)]);
            }
        }
    }

    public static LabyrinthGenerator getInstance()
    {
        if (instance == null)
        {
            instance = new LabyrinthGenerator();
        }
        return instance;
    }
}
