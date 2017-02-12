/**
 * This class was created by Rock Hymas. It's distributed as
 * part of the Strait Mod. Get the Source Code in github:
 * https://github.com/rockhymas/strait
 *
 * Strait is Open Source and distributed under the
 * CC-BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package com.gibraltar.strait.renderer;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.FMLLog;

import com.gibraltar.strait.entity.item.EntityFlatFrame;

public class RenderFlatFrameFactory implements IRenderFactory<EntityFlatFrame> {
    public Render<EntityFlatFrame> createRenderFor(RenderManager manager) {
        FMLLog.info("creating flat frame renderer");
        return new RenderFlatFrame(manager);
    }
}