package com.abecderic.labyrinth.command;

import com.abecderic.labyrinth.config.Config;
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
        subCommands = new HashMap<>();
        if (Config.getConfig().commands)
        {
            subCommands.put("tp-in", new CommandTpIn());
            subCommands.put("tp-out", new CommandTpOut());
            subCommands.put("chunk-info", new CommandChunkInfo());
            subCommands.put("spawn-mini", new CommandSpawnMini());
            subCommands.put("spawn-room", new CommandSpawnRoom());
            subCommands.put("save-template", new CommandSaveTemplate());
        }
    }

    @Override
    public String getName()
    {
        return "labyrinth";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return new TextComponentTranslation("command.usage").getUnformattedText();
    }

    @Override
    public List<String> getAliases()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add(getName());
        return list;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (!Config.getConfig().commands)
        {
            sender.sendMessage(new TextComponentTranslation("command.disabled"));
        }
        else if (args.length == 0)
        {
            sender.sendMessage(new TextComponentTranslation("command.usage"));
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
            sender.sendMessage(new TextComponentTranslation("command.usage"));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (args.length <= 1)
        {
            return new ArrayList<>(subCommands.keySet());
        }
        else
        {
            return new ArrayList<>();
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
        return o.getName().compareTo(getName());
    }
}
