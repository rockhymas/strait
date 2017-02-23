/**
 * This class was created by Rock Hymas. It's distributed as
 * part of the Strait Mod. Get the Source Code in github:
 * https://github.com/rockhymas/strait
 *
 * Strait is Open Source and distributed under the
 * CC-BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package com.gibraltar.strait.feature;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gibraltar.strait.entity.item.EntityFlatFrame;
import com.gibraltar.strait.renderer.RenderFlatFrameFactory;
import com.gibraltar.strait.strait;

public class FrameFeature extends Feature
{

    @Override
	public void preInit(FMLPreInitializationEvent event)
    {
		super.preInit(event);
        EntityRegistry.registerModEntity(new ResourceLocation("strait:flat_frame"), EntityFlatFrame.class, "strait:flat_frame", 1, strait.instance, 256, 64, false);
	}

    @Override
	public void preInitClient(FMLPreInitializationEvent event)
    {
		super.preInitClient(event);
        RenderingRegistry.registerEntityRenderingHandler(EntityFlatFrame.class, new RenderFlatFrameFactory());
	}

    @Override
    protected boolean hasSubscriptions()
    {
        return true;
    } 

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        ItemStack itemstack = event.getItemStack();
        EnumFacing facing = event.getFace();
        BlockPos blockpos = event.getPos().offset(facing);
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();

        if (event.getSide() == Side.CLIENT)
        {
            return;
        }

        if (!player.canPlayerEdit(blockpos, facing, itemstack))
        {
            return;
        }

        if (facing.getAxis() != EnumFacing.Axis.Y)
        {
            return;
        }

        if (itemstack.getItem() != Items.ITEM_FRAME)
        {
            return;
        }

        EntityHanging entityhanging = new EntityFlatFrame(world, blockpos, facing);

        if (entityhanging != null && entityhanging.onValidSurface())
        {
            if (!world.isRemote)
            {
                entityhanging.playPlaceSound();
                world.spawnEntity(entityhanging);
            }

            if (!player.capabilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }
        }
    }
}