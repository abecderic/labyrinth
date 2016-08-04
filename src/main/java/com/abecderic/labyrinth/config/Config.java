package com.abecderic.labyrinth.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config
{
    private static Config config;
    private int dimId;

    public static Config getConfig()
    {
        if (config == null)
            config = new Config();
        return config;
    }

    public void init(File file)
    {
        Configuration c = new Configuration(file);
        c.load();

        dimId = c.getInt("dimensionId", "general", 1024, Integer.MIN_VALUE, Integer.MAX_VALUE, "The dimension id used for the labyrinth dimension");

        if (c.hasChanged())
            c.save();
    }
}
