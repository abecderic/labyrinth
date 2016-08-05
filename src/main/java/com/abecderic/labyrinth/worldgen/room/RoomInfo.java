package com.abecderic.labyrinth.worldgen.room;

import java.util.List;

public final class RoomInfo
{
    public int weight;
    public List<Replacement> replacements;

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
}
