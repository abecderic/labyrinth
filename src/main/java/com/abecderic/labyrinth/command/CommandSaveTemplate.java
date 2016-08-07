package com.abecderic.labyrinth.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class CommandSaveTemplate implements ISubCommand
{
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            if (player.isCreative())
            {
                if (args.length > 7)
                {
                    try
                    {
                        int x1 = Integer.parseInt(args[1]);
                        int y1 = Integer.parseInt(args[2]);
                        int z1 = Integer.parseInt(args[3]);
                        int x2 = Integer.parseInt(args[4]);
                        int y2 = Integer.parseInt(args[5]);
                        int z2 = Integer.parseInt(args[6]);
                        String name = args[7];
                        BlockPos pos = new BlockPos(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));
                        BlockPos size = new BlockPos(Math.abs(x1 - x2), Math.abs(y1 - y2), Math.abs(z1 - z2)).add(1, 1, 1);

                        MinecraftServer minecraftserver = player.getServerWorld().getMinecraftServer();
                        TemplateManager templatemanager = player.getServerWorld().getStructureTemplateManager();
                        Template template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(name));
                        template.takeBlocksFromWorld(player.getEntityWorld(), pos, size, false, Blocks.field_189881_dj);
                        template.setAuthor(player.getDisplayNameString());
                        templatemanager.writeTemplate(minecraftserver, new ResourceLocation(name));
                    }
                    catch (NumberFormatException e)
                    {
                        sender.addChatMessage(new TextComponentTranslation("command.savetemplate.usage"));
                    }
                }
                else
                {
                    sender.addChatMessage(new TextComponentTranslation("command.savetemplate.usage"));
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
