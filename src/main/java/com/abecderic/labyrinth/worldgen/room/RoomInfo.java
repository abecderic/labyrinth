package com.abecderic.labyrinth.worldgen.room;

import com.abecderic.labyrinth.worldgen.LabyrinthChunk;
import net.minecraft.util.Rotation;

import java.util.List;

public final class RoomInfo
{
    public int weight;
    public LabyrinthChunk.Size[] size;
    public List<Replacement> replacements;
    public List<Transformation> transformations;

    public final class Replacement
    {
        public BlockWrapper original;
        public BlockWrapper[] replacement;
        public String type;
    }

    public final class BlockWrapper
    {
        public String name;
        public Property[] properties;
    }

    public final class Property
    {
        public String name;
        public String value;
    }

    public final class Transformation
    {
        public LabyrinthChunk.Size size;
        public Boolean exitNorth;
        public Boolean exitSouth;
        public Boolean exitEast;
        public Boolean exitWest;
        public String structure;
        public Rotation rotation;
    }
}
