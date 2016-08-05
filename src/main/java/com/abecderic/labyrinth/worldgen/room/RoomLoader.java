package com.abecderic.labyrinth.worldgen.room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RoomLoader
{
    private static Map<String, RoomInfo> roomInfos = new HashMap<String, RoomInfo>();
    private final Gson gson = new GsonBuilder().create();
    private String folder;

    public RoomLoader(String folder)
    {
        this.folder = folder;
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
        RoomInfo ri = gson.<RoomInfo>fromJson(new InputStreamReader(stream), new TypeToken<RoomInfo>(){}.getType());
        roomInfos.put(name, ri);
    }
}
