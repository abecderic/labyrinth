package com.abecderic.labyrinth.worldgen.room;

import com.abecderic.labyrinth.worldgen.LabyrinthChunk;

import java.util.List;

public final class RoomInfo
{
    public int weight;
    public Integer down;
    public Integer up;
    public LabyrinthChunk.Size[] size;
    public List<Replacement> replacements;
    public List<Transformation> transformations;

    public static final class Replacement
    {
        public BlockWrapper original;
        public BlockWrapper[] replacement;
        public String type;
    }

    public static final class BlockWrapper
    {
        public String name;
        public Property[] properties;
    }

    public static final class Property
    {
        public String name;
        public String value;
    }

    public static final class Transformation
    {
        public LabyrinthChunk.Size size;
        public Boolean exitNorth;
        public Boolean exitSouth;
        public Boolean exitEast;
        public Boolean exitWest;
        public String structure;
        public String rotation;
    }
}
