package com.abecderic.labyrinth.block;

import com.abecderic.labyrinth.Labyrinth;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDaedalus extends Block
{
    public BlockDaedalus()
    {
        super(Material.ROCK);
        setRegistryName(LabyrinthBlocks.DAEDALUS);
        setHardness(3.0f);
        setResistance(5.0f);
        setCreativeTab(CreativeTabs.SEARCH);
    }

    @Override
    public String getUnlocalizedName()
    {
        return "tile." + Labyrinth.MODID + ":" + LabyrinthBlocks.DAEDALUS;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        System.out.println("player " + playerIn.getName() + " clicked block");
        return true;
    }
}
