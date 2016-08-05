package com.abecderic.labyrinth.worldgen;

import net.minecraft.nbt.NBTTagCompound;

public class LabyrinthChunk
{
    private WallType north;
    private WallType west;
    private Size size;

    public LabyrinthChunk()
    {
        north = WallType.WALL;
        west = WallType.WALL;
        size = Size.SINGLE;
    }

    public LabyrinthChunk(boolean exitNorth, boolean exitWest)
    {
        north = WallType.WALL;
        west = WallType.WALL;
        size = Size.SINGLE;
        if (exitNorth) north = WallType.EXIT;
        if (exitWest) west = WallType.EXIT;
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        north = WallType.values()[nbt.getByte("wallNorth")];
        west = WallType.values()[nbt.getByte("wallWest")];
        size = Size.values()[nbt.getByte("size")];
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setByte("wallNorth", (byte)north.ordinal());
        nbt.setByte("wallWest", (byte)west.ordinal());
        nbt.setByte("size", (byte)size.ordinal());
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

    public void setNorth(WallType north)
    {
        this.north = north;
    }

    public void setWest(WallType west)
    {
        this.west = west;
    }

    public Size getSize()
    {
        return size;
    }

    public void setSize(Size size)
    {
        this.size = size;
    }

    public enum WallType
    {
        WALL,
        EXIT,
        OPEN;
    }

    public enum Size
    {
        SINGLE,
        X_2,
        X_3,
        X_4,
        DOUBLE,
        Y_2,
        Y_3,
        Y_4,
        TRIPLE;
    }
}
