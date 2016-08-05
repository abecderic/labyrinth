package com.abecderic.labyrinth.worldgen.algorithm;

public class Wall
{
    private int x;
    private int z;
    private boolean facing; /* true = north, false = west */

    public Wall(int x, int z, boolean facing)
    {
        this.x = x;
        this.z = z;
        this.facing = facing;
    }

    public int getX()
    {
        return x;
    }

    public int getZ()
    {
        return z;
    }

    public boolean facing()
    {
        return facing;
    }
}
