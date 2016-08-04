package com.abecderic.labyrinth.command;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.worldgen.LabyrinthChunk;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CommandSpawnMini implements ISubCommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            if (player.isCreative())
            {
                if (args.length >= 3)
                {
                    try
                    {
                        int x = Integer.parseInt(args[1]);
                        int z = Integer.parseInt(args[2]);
                        spawnMini(player.getEntityWorld(), player.getPosition(), x, z);
                    }
                    catch (NumberFormatException e)
                    {
                        sender.addChatMessage(new TextComponentTranslation("command.spawnmini.usage"));
                    }
                }
                else
                {
                    sender.addChatMessage(new TextComponentTranslation("command.spawnmini.usage"));
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

    private void spawnMini(World world, BlockPos pos, int x, int z)
    {
        System.out.println("Spawning...");
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                LabyrinthChunk chunk = Labyrinth.instance.worldData.getDataForChunk(x + i, z + j);
                world.setBlockState(pos.add(2 * i, -1, 2 * j), Blocks.WOOL.getDefaultState());
                if (chunk.getNorth() == LabyrinthChunk.WallType.WALL)
                    world.setBlockState(pos.add(2 * i + 1, -1, 2 * j), Blocks.WOOL.getDefaultState());
                if (chunk.getWest() == LabyrinthChunk.WallType.WALL)
                    world.setBlockState(pos.add(2 * i, -1, 2 * j + 1), Blocks.WOOL.getDefaultState());
            }
        }
        System.out.println("Done.");
    }
}
