package com.abecderic.labyrinth.config;

import com.abecderic.labyrinth.Labyrinth;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config
{
    private static Config config;
    public int dimId;
    public boolean generateRoof;
    public boolean sunlight;
    public boolean villageHouse;

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
        generateRoof = c.getBoolean("generateRoof", "general", true, "If the labyrinth should have a roof.");
        sunlight = c.getBoolean("sunlight", "general", false, "If the sun should shine in the labyrinth dimension.");
        villageHouse = c.getBoolean("villageHouse", "general", true, "If Daedalus' Workshop should spawn in villages.");
        Labyrinth.instance.roomLoader.init(c.getStringList("list", "rooms",
                new String[]{"empty_stonebrick", "empty_wood", "cave", "hallway", "workshop", "lab", "library", "grasslands", "desert", "canal", "farm", "checkpoint", "snow", "nether", "forest"},
                "A list of rooms in the labyrinth. Remove from here, if you don't want a room to spawn. Add here, if you added a room in a resource pack."));

        if (c.hasChanged())
            c.save();
    }
}
