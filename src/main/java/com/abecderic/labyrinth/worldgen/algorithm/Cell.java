package com.abecderic.labyrinth.worldgen.algorithm;

public class Cell
{
    private boolean[] exits = new boolean[4];

    public boolean hasExits()
    {
        for (int i = 0; i < exits.length; i++)
        {
            if (exits[i])
                return true;
        }
        return false;
    }

    public void addExit(int i)
    {
        exits[i] = true;
    }

    public boolean hasExit(int i)
    {
        return exits[i];
    }
}
