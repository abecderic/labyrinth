package com.abecderic.labyrinth.command;

import com.abecderic.labyrinth.worldgen.room.RoomGenerator;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandSpawnRoom implements ISubCommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            if (args.length > 1)
            {
                RoomGenerator.getInstance().generateRoomAt(player.getEntityWorld(), player.getPosition(), args[1]);
            }
            else
            {
                sender.addChatMessage(new TextComponentTranslation("command.spawnroom.usage"));
            }
        }
        else
        {
            sender.addChatMessage(new TextComponentTranslation("command.onlyplayers"));
        }
    }
}
