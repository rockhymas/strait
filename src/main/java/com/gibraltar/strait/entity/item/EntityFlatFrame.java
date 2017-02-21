/**
 * This class was created by Rock Hymas. It's distributed as
 * part of the Strait Mod. Get the Source Code in github:
 * https://github.com/rockhymas/strait
 *
 * Strait is Open Source and distributed under the
 * CC-BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package com.gibraltar.strait.entity.item;

import com.google.common.base.Predicate;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import com.gibraltar.strait.feature.FrameFeature;

public class EntityFlatFrame extends EntityItemFrame implements IEntityAdditionalSpawnData
{
    private static final Predicate<Entity> IS_HANGING_ENTITY = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof EntityHanging;
        }
    };

    public EnumFacing realFacingDirection;

    public EntityFlatFrame(World worldIn)
    {
        super(worldIn);
    }

    public EntityFlatFrame(World worldIn, BlockPos pos, EnumFacing facing)
    {
        super(worldIn, pos, facing);
        this.setSize(0.5F, 0.03125F);
    }

    private double offs(int i)
    {
        return i % 32 == 0 ? 0.5D : 0.0D;
    }

    @Override
    protected void updateFacingWithBoundingBox(EnumFacing facingDirectionIn)
    {
        Validate.notNull(facingDirectionIn);
        this.realFacingDirection = facingDirectionIn;
        this.facingDirection = EnumFacing.SOUTH;
        this.rotationYaw = realFacingDirection.getAxis() == EnumFacing.Axis.Y ? 0 : (float)(this.realFacingDirection.getHorizontalIndex() * 90);
        this.rotationPitch = realFacingDirection == EnumFacing.UP ? -90.0F : 90.0F;
        this.prevRotationYaw = this.rotationYaw;
        this.updateBoundingBox();
    }

    @Override
    public boolean onValidSurface()
    {
        if (this.realFacingDirection.getAxis() == EnumFacing.Axis.Y)
        {
            if (!this.worldObj.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty())
            {
                return false;
            }
            else
            {
                BlockPos blockpos = this.hangingPosition.offset(this.realFacingDirection.getOpposite());
                IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
                if (!iblockstate.isSideSolid(this.worldObj, blockpos, this.realFacingDirection))
                {                    
                    if (!iblockstate.getMaterial().isSolid() && !BlockRedstoneDiode.isDiode(iblockstate))
                    {
                        return false;
                    }
                }

                return this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), IS_HANGING_ENTITY).isEmpty();
            }
        }
        else
        {
            return super.onValidSurface();
        }
    }

    @Override
    protected void updateBoundingBox()
    {
        if (this.realFacingDirection == null)
        {
            return;
        }
        else if (this.realFacingDirection.getAxis() == EnumFacing.Axis.Y)
        {
            double d0 = (double)this.hangingPosition.getX() + 0.5D;
            double d1 = (double)this.hangingPosition.getY() + 0.5D;
            double d2 = (double)this.hangingPosition.getZ() + 0.5D;
            d1 = d1 - (double)this.realFacingDirection.getFrontOffsetY() * 0.46875D;

            double d6 = (double)this.getHeightPixels();
            double d7 = -(double)this.realFacingDirection.getFrontOffsetY();
            double d8 = (double)this.getHeightPixels();

            d6 = d6 / 32.0D;
            d7 = d7 / 32.0D;
            d8 = d8 / 32.0D;

            this.posX = d0;
            this.posY = d1 - d7;
            this.posZ = d2;
            this.height = 1.0F / 16.0F;
            this.setEntityBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
        }
        else
        {
            super.updateBoundingBox();
        }
    }

    @Override
	public void dropItemOrSelf(Entity entityIn, boolean p_146065_2_)
    {
		if(!p_146065_2_)
        {
			super.dropItemOrSelf(entityIn, p_146065_2_);
			return;
		}

		if (getEntityWorld().getGameRules().getBoolean("doEntityDrops"))
        {
			ItemStack itemstack = getDisplayedItem();

			if (entityIn instanceof EntityPlayer)
            {
				EntityPlayer entityplayer = (EntityPlayer)entityIn;

				if (entityplayer.capabilities.isCreativeMode)
                {
					removeFrameFromMap(itemstack);
					return;
				}
			}

            entityDropItem(new ItemStack(Items.ITEM_FRAME, 1), 0.0F);
		}
	}

	private void removeFrameFromMap(ItemStack stack)
    {
		if(stack != null && stack.stackSize > 0)
        {
			if(stack.getItem() instanceof ItemMap)
            {
				MapData mapdata = ((ItemMap) stack.getItem()).getMapData(stack, getEntityWorld());
				mapdata.mapDecorations.remove("frame-" + getEntityId());
			}

			stack.setItemFrame((EntityItemFrame) null);
		}
	}

    @Override
    public EntityItem entityDropItem(ItemStack stack, float offsetY)
    {
        EntityItem entityitem = new EntityItem(this.worldObj, this.posX + (double)((float)this.realFacingDirection.getFrontOffsetX() * 0.15F), this.posY + (double)offsetY, this.posZ + (double)((float)this.realFacingDirection.getFrontOffsetZ() * 0.15F), stack);
        entityitem.setDefaultPickupDelay();
        if (realFacingDirection == EnumFacing.DOWN)
        {
            entityitem.motionY = -entityitem.motionY;
        }
        this.worldObj.spawnEntityInWorld(entityitem);
        return entityitem;
    }

    @Override
    public void setEntityBoundingBox(AxisAlignedBB bb)
    {
        super.setEntityBoundingBox(bb);
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setByte("RealFacing", (byte)this.realFacingDirection.getIndex());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.updateFacingWithBoundingBox(EnumFacing.getFront(compound.getByte("RealFacing")));
    }

    @Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeShort(realFacingDirection.getIndex());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		updateFacingWithBoundingBox(EnumFacing.getFront(additionalData.readShort()));
	}
}