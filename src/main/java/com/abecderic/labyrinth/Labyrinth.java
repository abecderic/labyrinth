package com.abecderic.labyrinth;

import com.abecderic.labyrinth.block.LabyrinthBlocks;
import com.abecderic.labyrinth.command.LabyrinthCommand;
import com.abecderic.labyrinth.config.Config;
import com.abecderic.labyrinth.proxy.CommonProxy;
import com.abecderic.labyrinth.util.LabyrinthWorldData;
import com.abecderic.labyrinth.worldgen.LabyrinthWorldProvider;
import com.abecderic.labyrinth.worldgen.room.RoomLoader;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Labyrinth.MODID, name = Labyrinth.MODNAME, version = Labyrinth.VERSION)
public class Labyrinth
{
    public static final String MODID = "labyrinth";
    public static final String MODNAME = "Daedalus' Labyrinth";
    public static final String VERSION = "${version}";

    @Mod.Instance(value = Labyrinth.MODID)
    public static Labyrinth instance;

    @SidedProxy(clientSide = "com.abecderic.labyrinth.proxy.ClientProxy", serverSide = "com.abecderic.labyrinth.proxy.CommonProxy")
    public static CommonProxy proxy;

    public Logger logger;
    public RoomLoader roomLoader;
    public DimensionType dimensionType;
    public LabyrinthWorldData worldData;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = LogManager.getLogger(MODID);
        roomLoader = new RoomLoader("assets/" + MODID + "/structures/");
        Config.getConfig().init(event.getSuggestedConfigurationFile());
        LabyrinthBlocks.registerBlocks();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        dimensionType = DimensionType.register("labyrinth", "_labyrinth", "labyrinth".hashCode(), LabyrinthWorldProvider.class, false);
        DimensionManager.registerDimension(Config.getConfig().dimId, dimensionType);
        proxy.registerModels();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new LabyrinthCommand());
        worldData = (LabyrinthWorldData) event.getServer().worldServerForDimension(Config.getConfig().dimId).loadItemData(LabyrinthWorldData.class, MODID);
        if (worldData == null)
        {
            worldData = new LabyrinthWorldData(MODID);
            event.getServer().worldServerForDimension(Config.getConfig().dimId).setItemData(MODID, worldData);
        }
    }
}
