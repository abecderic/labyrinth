package com.abecderic.labyrinth.command;

import com.abecderic.labyrinth.config.Config;
import com.abecderic.labyrinth.util.LabyrinthTeleporterOut;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;

public class CommandTpOut implements ISubCommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (player.isCreative())
            {
                if (player.dimension == Config.getConfig().dimId)
                {
                    server.getPlayerList().transferPlayerToDimension(player, 0, new LabyrinthTeleporterOut((WorldServer) server.getEntityWorld()));
                }
                else
                {
                    sender.addChatMessage(new TextComponentTranslation("command.notinlabyrinth"));
                }
            }
            else
            {
                sender.addChatMessage(new TextComponentTranslation("command.onlycreative"));
            }
        }
        else
        {
            sender.addChatMessage(new TextComponentTranslation("command.onlyplayers"));
        }
    }
}
