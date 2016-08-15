package com.abecderic.labyrinth.command;

import com.abecderic.labyrinth.config.Config;
import com.abecderic.labyrinth.util.LabyrinthTeleporter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;

public class CommandTpIn implements ISubCommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            if (player.isCreative())
            {
                if (player.dimension == 0)
                {
                    server.getPlayerList().transferPlayerToDimension(player, Config.getConfig().dimId, new LabyrinthTeleporter((WorldServer) server.getEntityWorld()));
                }
                else
                {
                    sender.addChatMessage(new TextComponentTranslation("command.notinoverworld"));
                }
            }
        }
        else
        {
            sender.addChatMessage(new TextComponentTranslation("command.onlyplayers"));
        }
    }
}
