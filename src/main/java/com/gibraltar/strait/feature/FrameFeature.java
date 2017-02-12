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
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.gibraltar.strait.entity.item.EntityFlatFrame;
import com.gibraltar.strait.items.ItemFlatFrame;
import com.gibraltar.strait.renderer.RenderFlatFrameFactory;

public class FrameFeature extends Feature {
    public static Item flatFrame;

    @Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		flatFrame = new ItemFlatFrame();
	}

    @Override
	public void preInitClient(FMLPreInitializationEvent event) {
		super.preInitClient(event);
        ModelLoader.setCustomModelResourceLocation(flatFrame, 0, new ModelResourceLocation("strait:flat_frame", "inventory"));
        RenderingRegistry.registerEntityRenderingHandler(EntityItemFrame.class, new RenderFlatFrameFactory());
	}
}