package com.abecderic.labyrinth.worldgen;

import net.minecraft.nbt.NBTTagCompound;

public class LabyrinthChunk
{
    private WallType north;
    private WallType west;

    public LabyrinthChunk() {}

    public LabyrinthChunk(boolean exitNorth, boolean exitWest, boolean connectionNorth, boolean connectionWest)
    {
        north = WallType.WALL;
        west = WallType.WALL;
        if (exitNorth) north = WallType.EXIT;
        if (exitWest) west = WallType.EXIT;
        if (connectionNorth) north = WallType.OPEN;
        if (connectionWest) west = WallType.OPEN;
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        north = WallType.values()[nbt.getByte("wallNorth")];
        west = WallType.values()[nbt.getByte("wallWest")];
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setByte("wallNorth", (byte)north.ordinal());
        nbt.setByte("wallWest", (byte)west.ordinal());
        return nbt;
    }

    public WallType getNorth()
    {
        return north;
    }

    public WallType getWest()
    {
        return west;
    }

    public enum WallType
    {
        WALL,
        EXIT,
        OPEN;
    }
}
