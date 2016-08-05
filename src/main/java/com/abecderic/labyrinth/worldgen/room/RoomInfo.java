package com.abecderic.labyrinth.worldgen.room;

import com.abecderic.labyrinth.worldgen.LabyrinthChunk;

import java.util.List;

public final class RoomInfo
{
    public int weight;
    public String size;
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

        public final class Property
        {
            public String name;
            public String value;
        }
    }

    public final class Transformation
    {
        public LabyrinthChunk.Size size;
        public boolean exitNorth;
        public boolean exitSouth;
        public boolean exitWest;
        public boolean exitEast;
        public String structure;
        public String rotation;
    }
}
