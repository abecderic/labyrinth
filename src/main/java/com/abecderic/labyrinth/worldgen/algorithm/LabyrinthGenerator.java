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
                    chunks[i][j] = new LabyrinthChunk(cells[i][j].hasExit(EnumFacing.NORTH.getIndex() - 2), random.nextInt(EDGE_EXIT_CHANCE) == 0, false, false);
                }
                else if (j == 0)
                {
                    chunks[i][j] = new LabyrinthChunk(random.nextInt(EDGE_EXIT_CHANCE) == 0, cells[i][j].hasExit(EnumFacing.WEST.getIndex() - 2), false, false);
                }
                else
                {
                    chunks[i][j] = new LabyrinthChunk(cells[i][j].hasExit(EnumFacing.NORTH.getIndex() - 2), cells[i][j].hasExit(EnumFacing.WEST.getIndex() - 2), false, false);
                }
            }
        }
        return chunks;
    }

    public void createLabyrinth(int currentX, int currentZ)
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

    public static LabyrinthGenerator getInstance()
    {
        if (instance == null)
        {
            instance = new LabyrinthGenerator();
        }
        return instance;
    }
}
