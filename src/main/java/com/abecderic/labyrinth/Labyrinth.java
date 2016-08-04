package com.abecderic.labyrinth;

import com.abecderic.labyrinth.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Labyrinth.MODID, name = Labyrinth.MODNAME, version = Labyrinth.VERSION)
public class Labyrinth
{
    public static final String MODID = "labyrinth";
    public static final String MODNAME = "Labyrinth";
    public static final String VERSION = "${version}";

    @SidedProxy(clientSide = "com.abecderic.labyrinth.proxy.ClientProxy", serverSide = "com.abecderic.labyrinth.proxy.CommonProxy")
    public static CommonProxy proxy;

    public void init(FMLInitializationEvent event)
    {
        
    }
}
