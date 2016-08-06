package com.abecderic.labyrinth.proxy;

import com.abecderic.labyrinth.block.LabyrinthBlocks;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerModels()
    {
        LabyrinthBlocks.registerModels();
    }
}
