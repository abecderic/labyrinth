package com.abecderic.labyrinth.command;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.worldgen.LabyrinthChunk;
import net.minecraft.block.BlockCarpet;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
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
                        Labyrinth.instance.logger.info("Spawning a mini labyrinth at (" + player.getPosition().getX() + ", " + player.getPosition().getY() + ", " + player.getPosition().getZ() + ") in dimension " + player.dimension + " on behalf of " + player.getName());
                        spawnMini(player.getEntityWorld(), player.getPosition(), x, z);
                    }
                    catch (NumberFormatException e)
                    {
                        sender.sendMessage(new TextComponentTranslation("command.spawnmini.usage"));
                    }
                }
                else
                {
                    sender.sendMessage(new TextComponentTranslation("command.spawnmini.usage"));
                }
            }
            else
            {
                sender.sendMessage(new TextComponentTranslation("command.onlycreative"));
            }
        }
        else
        {
            sender.sendMessage(new TextComponentTranslation("command.onlyplayers"));
        }
    }

    private void spawnMini(World world, BlockPos pos, int x, int z)
    {
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                world.setBlockState(pos.add(2 * i, -2, 2 * j), Blocks.WOOL.getDefaultState());
                world.setBlockState(pos.add(2 * i + 1, -2, 2 * j), Blocks.WOOL.getDefaultState());
                world.setBlockState(pos.add(2 * i, -2, 2 * j + 1), Blocks.WOOL.getDefaultState());
                world.setBlockState(pos.add(2 * i + 1, -2, 2 * j + 1), Blocks.WOOL.getDefaultState());

                LabyrinthChunk chunk = Labyrinth.instance.worldData.getDataForChunk(x + i, z + j, world.rand);
                world.setBlockState(pos.add(2 * i, -1, 2 * j), Blocks.WOOL.getDefaultState());
                if (chunk.getNorth() == LabyrinthChunk.WallType.WALL)
                    world.setBlockState(pos.add(2 * i + 1, -1, 2 * j), Blocks.WOOL.getDefaultState());
                else if (chunk.getNorth() == LabyrinthChunk.WallType.OPEN)
                    world.setBlockState(pos.add(2 * i + 1, -1, 2 * j), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, getColorForSize(chunk.getSize())));
                else
                    world.setBlockState(pos.add(2 * i + 1, -1, 2 * j), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.GRAY));
                if (chunk.getWest() == LabyrinthChunk.WallType.WALL)
                    world.setBlockState(pos.add(2 * i, -1, 2 * j + 1), Blocks.WOOL.getDefaultState());
                else if (chunk.getWest() == LabyrinthChunk.WallType.OPEN)
                    world.setBlockState(pos.add(2 * i, -1, 2 * j + 1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, getColorForSize(chunk.getSize())));
                else
                    world.setBlockState(pos.add(2 * i, -1, 2 * j + 1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.GRAY));
                world.setBlockState(pos.add(2 * i + 1, -1, 2 * j + 1), Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, getColorForSize(chunk.getSize())));
            }
        }
    }

    private EnumDyeColor getColorForSize(LabyrinthChunk.Size size)
    {
        switch (size)
        {
            case X_2: return EnumDyeColor.PINK;
            case X_3: return EnumDyeColor.RED;
            case X_4: return EnumDyeColor.MAGENTA;
            case Z_2: return EnumDyeColor.LIGHT_BLUE;
            case Z_3: return EnumDyeColor.BLUE;
            case Z_4: return EnumDyeColor.CYAN;
            case DOUBLE: return EnumDyeColor.YELLOW;
            case TRIPLE: return EnumDyeColor.ORANGE;
            default: return EnumDyeColor.BLACK;
        }
    }
}
