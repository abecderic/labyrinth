package com.abecderic.labyrinth.worldgen.algorithm;

import com.abecderic.labyrinth.worldgen.LabyrinthChunk;

public class LabyrinthChunkWrapper
{
    private LabyrinthChunk chunk;
    private boolean part;

    public LabyrinthChunkWrapper()
    {
        chunk = new LabyrinthChunk();
        part = false;
    }

    public LabyrinthChunk getChunk()
    {
        return chunk;
    }

    public boolean isPartOfLabyrinth()
    {
        return part;
    }

    public void setPartOfLabyrinth(boolean part)
    {
        this.part = part;
    }
}
