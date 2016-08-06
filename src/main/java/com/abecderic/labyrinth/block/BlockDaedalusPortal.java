package com.abecderic.labyrinth.block;

import com.abecderic.labyrinth.Labyrinth;
import com.abecderic.labyrinth.util.LabyrinthTeleporterPortal;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockDaedalusPortal extends Block
{
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
    protected static final AxisAlignedBB ENTITY_AABB = new AxisAlignedBB(-3.0D, -2.0D, -3.0D, 4.0D, 2.0D, 4.0D);

    public BlockDaedalusPortal()
    {
        super(Material.PORTAL);
        setRegistryName(LabyrinthBlocks.PORTAL);
        setLightLevel(1.0f);
        setTickRandomly(true);
    }

    @Override
    public String getUnlocalizedName()
    {
        return "tile." + Labyrinth.MODID + ":" + LabyrinthBlocks.PORTAL;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss() && !worldIn.isRemote && entityIn.getEntityBoundingBox().intersectsWith(state.getBoundingBox(worldIn, pos).offset(pos)) && entityIn instanceof EntityPlayerMP)
        {
            if (worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() != LabyrinthBlocks.portal)
            {
                LabyrinthTeleporterPortal.getInstance().teleportEntity(worldIn.getMinecraftServer(), pos, (EntityPlayerMP) entityIn);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double d0 = (double)((float)pos.getX() + rand.nextFloat());
        double d1 = (double)((float)pos.getY() + 0.8F);
        double d2 = (double)((float)pos.getZ() + rand.nextFloat());
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
        if (!worldIn.isRemote)
        {
            if (worldIn.getBlockState(pos.add(0, 1, 0)).getBlock() != LabyrinthBlocks.portal && worldIn.getBlockState(pos.add(0, -1, 0)).getBlock() == LabyrinthBlocks.portal)
            {
                List<EntityLivingBase> list = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, ENTITY_AABB);
                if (list.isEmpty())
                {
                    worldIn.setBlockState(pos, LabyrinthBlocks.daedalus.getDefaultState().withProperty(BlockDaedalus.DELTA, true));
                    worldIn.setBlockState(pos.add(0, -1, 0), LabyrinthBlocks.daedalus.getDefaultState());
                    LabyrinthTeleporterPortal.getInstance().invalidateDestination(pos);
                }
            }
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
    {
        /* NO-OP */
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
