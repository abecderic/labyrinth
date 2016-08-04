package com.abecderic.labyrinth.worldgen;

import com.abecderic.labyrinth.Labyrinth;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

public class LabyrinthWorldProvider extends WorldProvider
{
    @Override
    public DimensionType getDimensionType()
    {
        return Labyrinth.instance.dimensionType;
    }
}
