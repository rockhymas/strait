/**
 * This class was created by Rock Hymas. It's distributed as
 * part of the Strait Mod. Get the Source Code in github:
 * https://github.com/rockhymas/strait
 *
 * Strait is Open Source and distributed under the
 * CC-BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package com.gibraltar.strait.items;

import javax.annotation.Nullable;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.gibraltar.strait.Reference;
import com.gibraltar.strait.entity.item.EntityFlatFrame;

public class ItemFlatFrame extends Item
{
    public ItemFlatFrame()
    {
        setUnlocalizedName("flat_frame");
        setCreativeTab(CreativeTabs.DECORATIONS);
        setRegistryName(Reference.MOD_PREFIX + "flat_frame");

        GameRegistry.register(this);
        GameRegistry.addShapelessRecipe(new ItemStack(this), Items.ITEM_FRAME, Items.SLIME_BALL);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        FMLLog.info("pos: " + pos); 
        BlockPos blockpos = pos.offset(facing);

        FMLLog.info("onItemUse");
        FMLLog.info("blockpos: " + blockpos);
        if (player.canPlayerEdit(blockpos, facing, itemstack))
        {
            EntityHanging entityhanging = new EntityFlatFrame(worldIn, blockpos, facing);
            FMLLog.info("facing a side, bounding box: " + entityhanging.getEntityBoundingBox());

            FMLLog.info("other blocks: " + worldIn.getCollisionBoxes(entityhanging, entityhanging.getEntityBoundingBox()).isEmpty());
            if (entityhanging != null && entityhanging.onValidSurface())
            {
                FMLLog.info("valid surface");
                if (!worldIn.isRemote)
                {
                    FMLLog.info("play sound, spawn entity");
                    entityhanging.playPlaceSound();
                    worldIn.spawnEntity(entityhanging);
                }

                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }
}