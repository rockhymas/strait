package com.gibraltar.strait.entity.item;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.apache.commons.lang3.Validate;

public class EntityFlatFrame extends EntityItemFrame implements IEntityAdditionalSpawnData
{
    public EntityFlatFrame(World worldIn)
    {
        super(worldIn);
        FMLLog.info("new flat frame in world");
    }

    public EntityFlatFrame(World worldIn, BlockPos pos, EnumFacing facing)
    {
        super(worldIn, pos, facing);
        FMLLog.info("new flat frame: " + facing);
    }

    private double offs(int i)
    {
        return i % 32 == 0 ? 0.5D : 0.0D;
    }

    @Override
    protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn)
    {
        Validate.notNull(facingDirectionIn);
        FMLLog.info("facingDirection: " + facingDirectionIn);
        this.facingDirection = facingDirectionIn;
        this.rotationYaw = facingDirection.getAxis() == EnumFacing.Axis.Y ? 0 : (float)(this.facingDirection.getHorizontalIndex() * 90);
        this.prevRotationYaw = this.rotationYaw;
        this.updateBoundingBox();
    }

    @Override
    public boolean onValidSurface()
    {
        if (this.facingDirection.getAxis() == EnumFacing.Axis.Y) {
            return true;
        }
        else
        {
            return super.onValidSurface();
        }
    }

    @Override
    protected void updateBoundingBox()
    {
        if (this.facingDirection == null)
        {
            return;
        }
        else if (this.facingDirection.getAxis() == EnumFacing.Axis.Y)
        {
            double d0 = (double)this.hangingPosition.getX() + 0.5D;
            double d1 = (double)this.hangingPosition.getY() + 0.5D;
            double d2 = (double)this.hangingPosition.getZ() + 0.5D;
            double d4 = this.offs(this.getWidthPixels());
            d0 = d0 - (double)this.facingDirection.getFrontOffsetX() * 0.46875D;
            d1 = d1 - (double)this.facingDirection.getFrontOffsetY() * 0.46875D;
            d2 = d2 - (double)this.facingDirection.getFrontOffsetZ() * 0.46875D;
            FMLLog.info("d0: " + d0 + ", " +
                        "d1: " + d1 + ", " +
                        "d2: " + d2 + ", ");

            // EnumFacing enumfacing = EnumFacing.EAST;
            // d0 = d0 + d4 * (double)enumfacing.getFrontOffsetX();
            // d2 = d2 + d4 * (double)enumfacing.getFrontOffsetZ();
            // d1 = d1 + d4 * (double)enumfacing.getFrontOffsetY();
            this.posX = d0;
            this.posY = d1;
            this.posZ = d2;
            double d6 = (double)this.getHeightPixels();
            double d7 = (double)this.getWidthPixels();
            double d8 = (double)this.getHeightPixels();

            d7 = 1.0D;

            d6 = d6 / 32.0D;
            d7 = d7 / 32.0D;
            d8 = d8 / 32.0D;
            FMLLog.info("d0: " + d0 + ", " +
                        "d1: " + d1 + ", " +
                        "d2: " + d2 + ", " +
                        "d6: " + d6 + ", " +
                        "d7: " + d7 + ", " +
                        "d8: " + d8 + ", "
                        );
            this.setEntityBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
        }
        else
        {
            super.updateBoundingBox();
        }
    }

    @Override
    public void setEntityBoundingBox(AxisAlignedBB bb)
    {
        super.setEntityBoundingBox(bb);
        FMLLog.info("flat frame bb: " + getEntityBoundingBox());
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setByte("Facing", (byte)this.facingDirection.getIndex());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.updateFacingWithBoundingBox(EnumFacing.getFront(compound.getByte("Facing")));
    }

    @Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeShort(facingDirection.getIndex());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		updateFacingWithBoundingBox(EnumFacing.getFront(additionalData.readShort()));
	}
}