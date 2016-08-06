package com.abecderic.labyrinth.worldgen.village;

import com.abecderic.labyrinth.block.BlockDaedalus;
import com.abecderic.labyrinth.block.LabyrinthBlocks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.List;
import java.util.Random;

public class VillageWorkshop extends StructureVillagePieces.House1
{
    public VillageWorkshop()
    {
        /* NO-OP */
    }

    public VillageWorkshop(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox boundingBox, EnumFacing facing)
    {
        super(start, type, rand, boundingBox, facing);
    }

    public static StructureVillagePieces.Village buildComponent(StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 9, 9, 6, facing);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new VillageWorkshop(startPiece, p5, random, structureboundingbox, facing) : null;

    }

    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
    {
        if (this.averageGroundLvl < 0)
        {
            this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

            if (this.averageGroundLvl < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 9 - 1, 0);
        }

        IBlockState blockOuter = this.getBiomeSpecificBlockState(Blocks.STONEBRICK.getDefaultState());
        IBlockState blockRoofNorth = this.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
        IBlockState blockRoofSouth = this.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
        IBlockState blockInner = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
        IBlockState blockWall = this.getBiomeSpecificBlockState(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH));

        // interior
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

        // ground
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 0, 5, blockOuter, blockOuter, false);

        // ceiling
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 8, 5, 5, blockOuter, blockInner, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 1, 8, 6, 4, blockInner, blockInner, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 7, 2, 8, 7, 3, blockInner, blockInner, false);

        // roof
        for (int i = -1; i <= 2; ++i)
        {
            for (int j = 0; j <= 8; ++j)
            {
                this.setBlockState(worldIn, blockRoofNorth, j, 6 + i, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, blockRoofSouth, j, 6 + i, 5 - i, structureBoundingBoxIn);
            }
        }

        // lower walls
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 5, blockOuter, blockOuter, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 8, 1, 5, blockOuter, blockOuter, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 0, 8, 1, 4, blockOuter, blockOuter, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 7, 1, 0, blockOuter, blockOuter, false);

        // corners
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 4, 0, blockOuter, blockOuter, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 5, 0, 4, 5, blockOuter, blockOuter, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 5, 8, 4, 5, blockOuter, blockOuter, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 0, 8, 4, 0, blockOuter, blockOuter, false);

        // walls
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 4, 4, blockWall, blockWall, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 7, 4, 5, blockWall, blockWall, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 1, 8, 4, 4, blockWall, blockWall, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 1, 4, 0, blockWall, blockWall, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 0, 7, 4, 0, blockWall, blockWall, false);

        // windows
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 3, 3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 6, 3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 3, 3, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 0, structureBoundingBoxIn);

        // labyrinth entrance
        this.setBlockState(worldIn, LabyrinthBlocks.daedalus.getDefaultState(), 7, 1, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, LabyrinthBlocks.daedalus.getDefaultState().withProperty(BlockDaedalus.DELTA, true), 7, 2, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, blockInner, 7, 3, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, blockInner, 7, 4, 1, structureBoundingBoxIn);

        // torches
        this.setBlockState(worldIn, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 2, 4, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 6, 4, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH), 2, 4, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH), 6, 4, 1, structureBoundingBoxIn);

        // misc interior
        this.setBlockState(worldIn, Blocks.QUARTZ_BLOCK.getDefaultState(), 7, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE.getDefaultState(), 7, 2, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.CRAFTING_TABLE.getDefaultState(), 1, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.FURNACE.getDefaultState(), 2, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.FURNACE.getDefaultState(), 3, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.CHEST.getDefaultState(), 3, 1, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.CHEST.getDefaultState(), 4, 1, 1, structureBoundingBoxIn);

        // entrance and path
        if (this.getBlockStateFromPos(worldIn, 1, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR)
        {
            this.setBlockState(worldIn, this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH)), 1, 0, -1, structureBoundingBoxIn);

            if (this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH)
            {
                this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 1, -1, -1, structureBoundingBoxIn);
            }
        }

        // below
        for (int l = 0; l < 6; ++l)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.clearCurrentPositionBlocksUpwards(worldIn, k, 9, l, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, blockOuter, k, -1, l, structureBoundingBoxIn);
            }
        }
        return true;
    }
}
