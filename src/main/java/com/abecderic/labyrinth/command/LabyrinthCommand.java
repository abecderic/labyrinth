package com.abecderic.labyrinth.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabyrinthCommand implements ICommand
{
    private Map<String, ISubCommand> subCommands;

    public LabyrinthCommand()
    {
        subCommands = new HashMap<String, ISubCommand>();
        subCommands.put("tp-in", new CommandTpIn());
    }

    @Override
    public String getCommandName()
    {
        return "labyrinth";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return new TextComponentTranslation("command.usage", "").getUnformattedText();
    }

    @Override
    public List<String> getCommandAliases()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add(getCommandName());
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args == null || args.length == 0)
        {
            sender.addChatMessage(new TextComponentTranslation("command.usage", ""));
            return;
        }
        else
        {
            for (Map.Entry<String, ISubCommand> entry : subCommands.entrySet())
            {
                if (args[0].equalsIgnoreCase(entry.getKey()))
                {
                    entry.getValue().execute(server, sender, args);
                    return;
                }
            }
            sender.addChatMessage(new TextComponentTranslation("command.usage", ""));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args == null || args.length <= 1)
        {
            List<String> list = new ArrayList<String>();
            for (String s : subCommands.keySet())
                list.add(s);
            return list;
        }
        else
        {
            return new ArrayList<String>();
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return false;
    }

    @Override
    public int compareTo(ICommand o)
    {
        return o.getCommandName().compareTo(getCommandName());
    }
}
