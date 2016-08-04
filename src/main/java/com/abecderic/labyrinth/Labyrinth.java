package com.abecderic.labyrinth;

import com.abecderic.labyrinth.command.LabyrinthCommand;
import com.abecderic.labyrinth.config.Config;
import com.abecderic.labyrinth.proxy.CommonProxy;
import com.abecderic.labyrinth.worldgen.LabyrinthWorldProvider;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Labyrinth.MODID, name = Labyrinth.MODNAME, version = Labyrinth.VERSION)
public class Labyrinth
{
    public static final String MODID = "labyrinth";
    public static final String MODNAME = "Labyrinth";
    public static final String VERSION = "${version}";

    @Mod.Instance(value = Labyrinth.MODID)
    public static Labyrinth instance;

    @SidedProxy(clientSide = "com.abecderic.labyrinth.proxy.ClientProxy", serverSide = "com.abecderic.labyrinth.proxy.CommonProxy")
    public static CommonProxy proxy;

    public DimensionType dimensionType;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Config.getConfig().init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        dimensionType = DimensionType.register("laybrinth", "labyrinth", "labyrinth".hashCode(), LabyrinthWorldProvider.class, false);
        DimensionManager.registerDimension(Config.getConfig().dimId, dimensionType);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new LabyrinthCommand());
    }
}
