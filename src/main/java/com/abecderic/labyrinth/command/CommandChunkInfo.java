package com.abecderic.labyrinth.command;

import com.abecderic.labyrinth.Labyrinth;
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
            EntityPlayerMP player = (EntityPlayerMP)sender;
            int x = (int)Math.floor(player.getPosition().getX() / 16D);
            int z = (int)Math.floor(player.getPosition().getZ() / 16D);
            player.addChatComponentMessage(new TextComponentTranslation("command.chunkinfo", x + "", z + ""));
            LabyrinthChunk chunk = Labyrinth.instance.worldData.getDataForChunk(x, z);
            player.addChatComponentMessage(new TextComponentTranslation("command.chunkinfo.north", chunk.getNorth().toString().toLowerCase()));
            player.addChatComponentMessage(new TextComponentTranslation("command.chunkinfo.west", chunk.getWest().toString().toLowerCase()));
        }
        else
        {
            sender.addChatMessage(new TextComponentTranslation("command.onlyplayers"));
        }
    }
}
