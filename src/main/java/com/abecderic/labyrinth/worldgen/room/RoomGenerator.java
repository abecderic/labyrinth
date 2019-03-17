package com.abecderic.labyrinth.worldgen.room;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.worldgen.LabyrinthChunk;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoomGenerator
{
    private static RoomGenerator instance;
    private TemplateManager manager;

    private RoomGenerator()
    {
        manager = new TemplateManager("config/" + Labyrinth.MODID + "/structures/", new DataFixer(0));
    }

    public void generateRoomAt(World world, int chunkX, int y, int chunkZ, String name, LabyrinthChunk.Size size, boolean exitNorth, boolean exitSouth, boolean exitEast, boolean exitWest)
    {
        RoomInfo ri = Labyrinth.instance.roomLoader.getInfo(name);
        if (ri == null)
        {
            Labyrinth.instance.logger.error("Tried to generate room \"" + name + "\" at chunkX=" + chunkX + ", chunkZ=" + chunkZ + " but room info is null. The files should be at .minecraft/config/" + Labyrinth.MODID + "/structures/");
            return;
        }
        if (ri.down != null && ri.down > 0)
        {
            y -= ri.down;
        }

        PlacementSettings settings = new PlacementSettings();
        if (ri.transformations != null)
        {
            for (RoomInfo.Transformation t : ri.transformations)
            {
                if (transformationFits(t, size, exitNorth, exitSouth, exitEast, exitWest))
                {
                    name = addTransformation(t, settings, name, world.rand);
                    break;
                }
            }
        }
        Template template = manager.getTemplate(world.getMinecraftServer(), new ResourceLocation("labyrinth:" + name));

        int structureX = chunkX * 16;
        int structureZ = chunkZ * 16;
        int defaultStructureX = structureX + 1;
        int defaultStructureZ = structureZ + 1;
        switch (settings.getRotation())
        {
            case CLOCKWISE_90:
                structureX += size.getX() * 16 - 1;
                structureZ += 1;
                break;
            case CLOCKWISE_180:
                structureX += size.getX() * 16 - 1;
                structureZ += size.getZ() * 16 - 1;
                break;
            case COUNTERCLOCKWISE_90:
                structureX += 1;
                structureZ += size.getZ() * 16 - 1;
                break;
            default:
                structureX += 1;
                structureZ += 1;
                break;
        }

        template.addBlocksToWorld(world, new BlockPos(structureX, y, structureZ), settings);

        if (ri != null)
        {
            if (ri.replacements != null)
            {
                for (RoomInfo.Replacement replacement : ri.replacements)
                {
                    try
                    {
                        applyReplacement(world, defaultStructureX, y, defaultStructureZ, template, replacement);
                    }
                    catch (NumberInvalidException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void applyReplacement(World world, int x, int y, int z, Template template, RoomInfo.Replacement replacement) throws NumberInvalidException
    {
        IBlockState originalState = getBlockState(replacement.original, world.rand);
        IBlockState replacementState = Blocks.AIR.getDefaultState();
        if (replacement.type.equalsIgnoreCase("all"))
        {
            replacementState = getBlockState(replacement.replacement[world.rand.nextInt(replacement.replacement.length)], world.rand);
        }
        for (int i = x; i < x + template.getSize().getX(); i++)
        {
            for (int j = y; j < y + template.getSize().getY(); j++)
            {
                for (int k = z; k < z + template.getSize().getZ(); k++)
                {
                    if (world.getBlockState(new BlockPos(i, j, k)) == originalState && replacement.type.equalsIgnoreCase("all"))
                    {
                        world.setBlockState(new BlockPos(i, j, k), replacementState);
                    }
                    else if (world.getBlockState(new BlockPos(i, j, k)) == originalState && replacement.type.equalsIgnoreCase("single"))
                    {
                        replacementState = getBlockState(replacement.replacement[world.rand.nextInt(replacement.replacement.length)], world.rand);
                        world.setBlockState(new BlockPos(i, j, k), replacementState);
                    }
                }
            }
        }
    }

    private IBlockState getBlockState(RoomInfo.BlockWrapper blockWrapper, Random rand)
    {
        if (blockWrapper.name.startsWith("-"))
        {
            if (blockWrapper.name.startsWith("-ore:"))
            {
                String oreName = blockWrapper.name.substring("-ore:".length());
                List<ItemStack> unmodifiableList = OreDictionary.getOres(oreName);
                List<ItemStack> list = new ArrayList<ItemStack>(unmodifiableList);
                for (int i = list.size() - 1; i >= 0; i--)
                {
                    if (!(list.get(i).getItem() instanceof ItemBlock))
                    {
                        list.remove(i);
                    }
                }
                if (!list.isEmpty())
                {
                    ItemStack stack = list.get(rand.nextInt(list.size()));
                    Block block = Block.getBlockFromItem(stack.getItem());
                    if (block != null)
                    {
                        return block.getDefaultState();
                    }
                }
            }
            else if (blockWrapper.name.startsWith("-ore-prefix:"))
            {
                String orePrefix = blockWrapper.name.substring("-ore-prefix:".length());
                List<String> ores = new ArrayList<String>();
                String[] oreDict = OreDictionary.getOreNames();
                for (String ore : oreDict)
                {
                    if (ore.startsWith(orePrefix))
                        ores.add(ore);
                }
                if (!ores.isEmpty())
                {
                    String ore = ores.get(rand.nextInt(ores.size()));
                    RoomInfo.BlockWrapper bw = new RoomInfo.BlockWrapper();
                    bw.name = "-ore:" + ore;
                    return getBlockState(bw, rand);
                }
            }
            return Blocks.AIR.getDefaultState();
        }
        else
        {
            Block block = Block.REGISTRY.getObject(new ResourceLocation(blockWrapper.name));
            IBlockState state = block.getDefaultState();
            if (blockWrapper.properties != null)
            {
                for (RoomInfo.Property property : blockWrapper.properties)
                {
                    IProperty<?> p = block.getBlockState().getProperty(property.name);
                    if (p != null)
                    {
                        state = addBlockStateProperty(state, p, property.value);
                    }
                }
            }
            return state;
        }
    }

    private <T extends Comparable<T>> IBlockState addBlockStateProperty(IBlockState state, IProperty<T> property, String value)
    {
        return state.withProperty(property, property.parseValue(value).get());
    }

    private boolean transformationFits(RoomInfo.Transformation t, LabyrinthChunk.Size size, boolean exitNorth, boolean exitSouth, boolean exitEast, boolean exitWest)
    {
        if (t.size != null && t.size != size)
            return false;
        if (t.exitNorth != null && t.exitNorth != exitNorth)
            return false;
        if (t.exitSouth != null && t.exitSouth != exitSouth)
            return false;
        if (t.exitEast != null && t.exitEast != exitEast)
            return false;
        if (t.exitWest != null && t.exitWest != exitWest)
            return false;
        return true;
    }

    private String addTransformation(RoomInfo.Transformation t, PlacementSettings settings, String name, Random rand)
    {
        if (t.structure != null)
            name = t.structure;
        if (t.rotation != null)
        {
            if (t.rotation.equalsIgnoreCase("any"))
            {
                settings.setRotation(Rotation.values()[rand.nextInt(Rotation.values().length)]);
            }
            else
            {
                settings.setRotation(Rotation.valueOf(t.rotation));
            }
        }
        return name;
    }

    public static RoomGenerator getInstance()
    {
        if (instance == null)
        {
            instance = new RoomGenerator();
        }
        return instance;
    }
}
