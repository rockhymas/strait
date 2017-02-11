package com.gibraltar.strait.entity.item;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityFlatFrame extends EntityItemFrame
{
    public EntityFlatFrame(World worldIn)
    {
        super(worldIn);
    }

    public EntityFlatFrame(World worldIn, BlockPos pos, EnumFacing facing)
    {
        super(worldIn, pos, facing);
    }
}