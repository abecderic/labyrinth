package com.abecderic.labyrinth.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config
{
    private static Config config;
    public int dimId;
    public boolean generateRoof;
    public boolean sunlight;

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
        // TODO set this to true sometime
        generateRoof = c.getBoolean("generateRoof", "general", false, "If the labyrinth should have a roof.");
        // TODO set this to false sometime
        sunlight = c.getBoolean("sunlight", "general", true, "If the sun should shine in the labyrinth dimension.");

        if (c.hasChanged())
            c.save();
    }
}
