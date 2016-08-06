package com.abecderic.labyrinth.command;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.config.Config;
import com.abecderic.labyrinth.worldgen.LabyrinthChunk;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandChunkInfo implements ISubCommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (player.dimension == Config.getConfig().dimId)
            {
                int x = (int) Math.floor(player.getPosition().getX() / 16D);
                int z = (int) Math.floor(player.getPosition().getZ() / 16D);
                player.addChatComponentMessage(new TextComponentTranslation("command.chunkinfo", x + "", z + ""));
                LabyrinthChunk chunk = Labyrinth.instance.worldData.getDataForChunk(x, z, sender.getEntityWorld().rand);
                LabyrinthChunk chunkEast = Labyrinth.instance.worldData.getDataForChunk(x + 1, z, sender.getEntityWorld().rand);
                LabyrinthChunk chunkSouth = Labyrinth.instance.worldData.getDataForChunk(x, z + 1, sender.getEntityWorld().rand);
                player.addChatComponentMessage(new TextComponentTranslation("command.chunkinfo.name", chunk.getName()));
                player.addChatComponentMessage(new TextComponentTranslation("command.chunkinfo.walls", chunk.getNorth().toString().toLowerCase(), chunk.getWest().toString().toLowerCase(), chunkEast.getWest().toString().toLowerCase(), chunkSouth.getNorth().toString().toLowerCase()));
                player.addChatComponentMessage(new TextComponentTranslation("command.chunkinfo.size", chunk.getSize().toString()));
            }
            else
            {
                sender.addChatMessage(new TextComponentTranslation("command.notinlabyrinth"));
            }
        }
        else
        {
            sender.addChatMessage(new TextComponentTranslation("command.onlyplayers"));
        }
    }
}
