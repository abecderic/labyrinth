package com.abecderic.labyrinth.worldgen;

import net.minecraft.nbt.NBTTagCompound;

public class LabyrinthChunk
{
    private int x;
    private int z;
    private WallType north;
    private WallType west;
    private Size size;
    private String name;

    public LabyrinthChunk(int x, int z)
    {
        this.x = x;
        this.z = z;
        north = WallType.WALL;
        west = WallType.WALL;
        size = Size.SINGLE;
        this.name = "null";
    }

    public LabyrinthChunk(int x, int z, boolean exitNorth, boolean exitWest)
    {
        this.x = x;
        this.z = z;
        north = WallType.WALL;
        west = WallType.WALL;
        size = Size.SINGLE;
        if (exitNorth) north = WallType.EXIT;
        if (exitWest) west = WallType.EXIT;
        this.name = "null";
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        north = WallType.values()[nbt.getByte("wallNorth")];
        west = WallType.values()[nbt.getByte("wallWest")];
        size = Size.values()[nbt.getByte("size")];
        name = nbt.getString("name");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setByte("wallNorth", (byte) north.ordinal());
        nbt.setByte("wallWest", (byte) west.ordinal());
        nbt.setByte("size", (byte) size.ordinal());
        nbt.setString("name", name);
        return nbt;
    }

    public int getX()
    {
        return x;
    }

    public int getZ()
    {
        return z;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public enum WallType
    {
        WALL,
        EXIT,
        OPEN;
    }

    public enum Size
    {
        SINGLE(1, 1),
        X_2(2, 1),
        X_3(3, 1),
        X_4(4, 1),
        DOUBLE(2, 2),
        Z_2(1, 2),
        Z_3(1, 3),
        Z_4(1, 4),
        TRIPLE(3, 3);

        private int x;
        private int z;

        Size(int x, int z)
        {
            this.x = x;
            this.z = z;
        }

        public int getX()
        {
            return x;
        }

        public int getZ()
        {
            return z;
        }
    }
}
