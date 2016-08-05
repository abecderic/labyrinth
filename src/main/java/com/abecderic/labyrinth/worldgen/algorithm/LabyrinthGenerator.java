package com.abecderic.labyrinth.worldgen.algorithm;

import com.abecderic.labyrinth.worldgen.LabyrinthChunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LabyrinthGenerator
{
    private static final int EDGE_EXIT_CHANCE = 4;
    private static LabyrinthGenerator instance;

    public LabyrinthChunk[][] createLabyrinth(Random random)
    {
        LabyrinthChunkWrapper[][] grid = new LabyrinthChunkWrapper[16][16];
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                grid[i][j] = new LabyrinthChunkWrapper();
            }
        }

        List<Wall> wallList = new ArrayList<Wall>();
        int i = 0;//random.nextInt(16);
        int j = 0;//random.nextInt(16);
        LabyrinthChunkWrapper cell = grid[i][j];
        cell.setPartOfLabyrinth(true);
        addWallsOfCell(i, j, wallList);

        while (wallList.size() > 0)
        {
            i = random.nextInt(wallList.size());
            Wall wall = wallList.get(i);
            if (!wall.facing()) /* west */
            {
                if (wall.getX() == 0) /* west edge of l-region */
                {
                    if (random.nextInt(EDGE_EXIT_CHANCE) == 0)
                    {
                        grid[wall.getX()][wall.getZ()].getChunk().setWest(LabyrinthChunk.WallType.EXIT);
                    }
                }
                else if (!grid[wall.getX()][wall.getZ()].isPartOfLabyrinth() && grid[wall.getX() - 1][wall.getZ()].isPartOfLabyrinth())
                {
                    grid[wall.getX()][wall.getZ()].setPartOfLabyrinth(true);
                    grid[wall.getX() - 1][wall.getZ()].getChunk().setWest(LabyrinthChunk.WallType.EXIT);
                    addWallsOfCell(wall.getX(), wall.getZ(), wallList);
                }
                else if (grid[wall.getX()][wall.getZ()].isPartOfLabyrinth() && !grid[wall.getX() - 1][wall.getZ()].isPartOfLabyrinth())
                {
                    grid[wall.getX() - 1][wall.getZ()].setPartOfLabyrinth(true);
                    grid[wall.getX() - 1][wall.getZ()].getChunk().setWest(LabyrinthChunk.WallType.EXIT);
                    addWallsOfCell(wall.getX() - 1, wall.getZ(), wallList);
                }
            }
            else /* north */
            {
                if (wall.getZ() == 0) /* north edge of l-region */
                {
                    if (random.nextInt(EDGE_EXIT_CHANCE) == 0)
                    {
                        grid[wall.getX()][wall.getZ()].getChunk().setNorth(LabyrinthChunk.WallType.EXIT);
                    }
                }
                else if (!grid[wall.getX()][wall.getZ()].isPartOfLabyrinth() && grid[wall.getX()][wall.getZ() - 1].isPartOfLabyrinth())
                {
                    grid[wall.getX()][wall.getZ()].setPartOfLabyrinth(true);
                    grid[wall.getX()][wall.getZ() - 1].getChunk().setNorth(LabyrinthChunk.WallType.EXIT);
                    addWallsOfCell(wall.getX(), wall.getZ(), wallList);
                }
                else if (grid[wall.getX()][wall.getZ()].isPartOfLabyrinth() && !grid[wall.getX()][wall.getZ() - 1].isPartOfLabyrinth())
                {
                    grid[wall.getX()][wall.getZ() - 1].setPartOfLabyrinth(true);
                    grid[wall.getX()][wall.getZ() - 1].getChunk().setNorth(LabyrinthChunk.WallType.EXIT);
                    addWallsOfCell(wall.getX(), wall.getZ() - 1, wallList);
                }
            }
            wallList.remove(i);
        }

        LabyrinthChunk[][] chunks = new LabyrinthChunk[16][16];
        for (i = 0; i < 16; i++)
        {
            for (j = 0; j < 16; j++)
            {
                chunks[i][j] = grid[15 - i][j].getChunk();
            }
        }
        return chunks;
    }

    private void addWallsOfCell(int x, int z, List<Wall> wallList)
    {
        wallList.add(new Wall(x, z, true));
        wallList.add(new Wall(x, z, false));
        if (z < 15)
            wallList.add(new Wall(x, z + 1, true));
        if (x < 15)
            wallList.add(new Wall(x + 1, z, false));
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
