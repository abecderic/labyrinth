package com.abecderic.labyrinth.util;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.worldgen.LabyrinthChunk;
import com.abecderic.labyrinth.worldgen.algorithm.LabyrinthGenerator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LabyrinthWorldData extends WorldSavedData
{
    private Map<String, LabyrinthChunk> dataMap;

    public LabyrinthWorldData(String name)
    {
        super(name);
        dataMap = new HashMap<String, LabyrinthChunk>();
    }

    public synchronized LabyrinthChunk getDataForChunk(int x, int z, Random random)
    {
        String key = x + "," + z;
        LabyrinthChunk chunk = dataMap.get(key);
        if (chunk == null)
        {
            Labyrinth.instance.logger.info("tried to get data for chunk " + key + "; generating data for region " + (x >> 4) + "," + (z >> 4));
            LabyrinthChunk[][] chunks = LabyrinthGenerator.getInstance().createLabyrinth(random);
            for (int i = 0; i < 16; i++)
            {
                for (int j = 0; j < 16; j++)
                {
                    dataMap.put((x + i) + "," + (z + j), chunks[i][j]);
                }
            }
            markDirty();
            return getDataForChunk(x, z, random);
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
            String[] parts = s.split(",");
            LabyrinthChunk chunk = new LabyrinthChunk(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
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
