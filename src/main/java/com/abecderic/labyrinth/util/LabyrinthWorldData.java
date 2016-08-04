package com.abecderic.labyrinth.util;

import com.abecderic.labyrinth.worldgen.LabyrinthChunk;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import java.util.HashMap;
import java.util.Map;

public class LabyrinthWorldData extends WorldSavedData
{
    private Map<String, LabyrinthChunk> dataMap;

    public LabyrinthWorldData(String name)
    {
        super(name);
        dataMap = new HashMap<String, LabyrinthChunk>();
    }

    public LabyrinthChunk getDataForChunk(int x, int z)
    {
        String key = x + "," + z;
        LabyrinthChunk chunk = dataMap.get(key);
        if (chunk == null)
        {
            //System.out.println("tried to get data for chunk " + key + "; generating data for region " + (x >> 4) + "," + (z >> 4));
            chunk = new LabyrinthChunk(Math.random() < 0.5, Math.random() < 0.5, Math.random() < 0.2, Math.random() < 0.2);
            dataMap.put(key, chunk);
            markDirty();
            return chunk;
        }
        else
        {
            return chunk;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        for (String s : nbt.getKeySet())
        {
            LabyrinthChunk chunk = new LabyrinthChunk();
            chunk.readFromNBT(nbt.getCompoundTag(s));
            dataMap.put(s, chunk);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        for (Map.Entry<String, LabyrinthChunk> entry : dataMap.entrySet())
        {
            nbt.setTag(entry.getKey(), entry.getValue().writeToNBT(new NBTTagCompound()));
        }
        return nbt;
    }
}
