package com.abecderic.labyrinth.block;

import com.abecderic.labyrinth.Labyrinth;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDaedalus extends Block
{
    public static final PropertyBool DELTA = PropertyBool.create("delta");

    public BlockDaedalus()
    {
        super(Material.ROCK);
        setRegistryName(LabyrinthBlocks.DAEDALUS);
        setHardness(50.0f);
        setResistance(2000.0f);
        setCreativeTab(CreativeTabs.SEARCH);
        setDefaultState(blockState.getBaseState().withProperty(DELTA, false));
    }

    @Override
    public String getUnlocalizedName()
    {
        return "tile." + Labyrinth.MODID + ":" + LabyrinthBlocks.DAEDALUS;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            if (playerIn.isCreative() && playerIn.getHeldItem(hand).getItem() == Items.STICK)
            {
                if (worldIn.getBlockState(pos).getValue(DELTA))
                {
                    worldIn.setBlockState(pos, state.withProperty(DELTA, false));
                }
                else
                {
                    worldIn.setBlockState(pos, state.withProperty(DELTA, true));
                }
            }
            else if (state.getValue(DELTA) && worldIn.getBlockState(pos.add(0, -1, 0)).getBlock() == LabyrinthBlocks.daedalus)
            {
                worldIn.setBlockState(pos, LabyrinthBlocks.portal.getDefaultState());
                worldIn.setBlockState(pos.add(0, -1, 0), LabyrinthBlocks.portal.getDefaultState());
            }
        }
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, DELTA);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(DELTA, (meta & 1) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(DELTA) ? 1 : 0);
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
}
