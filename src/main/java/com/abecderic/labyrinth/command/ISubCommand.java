package com.abecderic.labyrinth.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public interface ISubCommand
{
    void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;
}
