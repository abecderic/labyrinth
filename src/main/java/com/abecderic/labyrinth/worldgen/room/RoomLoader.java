package com.abecderic.labyrinth.worldgen.room;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.worldgen.LabyrinthChunk;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

public class RoomLoader
{
    private Map<String, RoomInfo> roomInfos = new HashMap<String, RoomInfo>();
    private Map<LabyrinthChunk.Size, List<String>> weightedList = new HashMap<LabyrinthChunk.Size, List<String>>();
    private final Gson gson = new GsonBuilder().create();
    private String folder;

    public RoomLoader(String folder)
    {
        this.folder = folder;
    }

    public void init(String[] rooms)
    {
        for (String room : rooms)
        {
            roomInfos.put(room, getInfo(room));
        }
        Labyrinth.instance.logger.info("Finished reading all rooms");
    }

    public RoomInfo getInfo(String name)
    {
        if (roomInfos.containsKey(name))
        {
            return roomInfos.get(name);
        }
        else
        {
            readInfo(name);
        }
        return roomInfos.containsKey(name) ? roomInfos.get(name) : null;
    }

    public String getRoom(LabyrinthChunk.Size size, Random random)
    {
        if (weightedList.containsKey(size))
        {
            return weightedList.get(size).get(random.nextInt(weightedList.get(size).size()));
        }
        else
        {
            return null;
        }
    }

    private void readInfo(String name)
    {
        File file = new File(this.folder, name + ".json");
        if (!file.exists())
        {
            readFileFromJar(name);
        }
        else
        {
            InputStream inputstream = null;
            try
            {
                inputstream = new FileInputStream(file);
                readFileFromStream(name, inputstream);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
            finally
            {
                IOUtils.closeQuietly(inputstream);
            }
        }
    }

    private void readFileFromJar(String name)
    {
        InputStream inputstream = null;
        try
        {
            inputstream = MinecraftServer.class.getResourceAsStream("/assets/labyrinth/structures/" + name + ".json");
            readFileFromStream(name, inputstream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(inputstream);
        }
    }

    private void readFileFromStream(String name, InputStream stream) throws IOException
    {
        if (stream == null)
        {
            Labyrinth.instance.logger.error("Can not read room info for " + name + " (stream is null); is the file in the correct location?");
        }
        else
        {
            Labyrinth.instance.logger.info("Reading room info for " + name);
            RoomInfo ri = gson.<RoomInfo>fromJson(new InputStreamReader(stream), new TypeToken<RoomInfo>(){}.getType());
            roomInfos.put(name, ri);
            for (LabyrinthChunk.Size size : ri.size)
            {
                if (weightedList.get(size) == null)
                {
                    weightedList.put(size, new ArrayList<String>());
                }
                for (int i = 0; i < ri.weight; i++)
                {
                    weightedList.get(size).add(name);
                }
            }
        }
    }
}
