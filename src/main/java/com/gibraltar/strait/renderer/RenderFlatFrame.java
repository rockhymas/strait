/**
 * This class was created by Rock Hymas. It's distributed as
 * part of the Strait Mod. Get the Source Code in github:
 * https://github.com/rockhymas/strait
 *
 * Strait is Open Source and distributed under the
 * CC-BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package com.gibraltar.strait.renderer;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCompass;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gibraltar.strait.entity.item.EntityFlatFrame;

@SideOnly(Side.CLIENT)
public class RenderFlatFrame extends RenderItemFrame
{
    private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
    private final Minecraft mc = Minecraft.getMinecraft();
    private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
    private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
    private final RenderItem itemRenderer;

    public RenderFlatFrame(RenderManager renderManagerIn)
    {
        super(renderManagerIn, Minecraft.getMinecraft().getRenderItem());
        this.itemRenderer = mc.getRenderItem();
    }

    @Override
    public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        EntityFlatFrame entityFF = (EntityFlatFrame) entity;
        GlStateManager.pushMatrix();
        BlockPos blockpos = entityFF.getHangingPosition();
        double d0 = (double)blockpos.getX() - entityFF.posX + x;
        double d1 = (double)blockpos.getY() - entityFF.posY + y;
        double d2 = (double)blockpos.getZ() - entityFF.posZ + z;
        GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        GlStateManager.rotate(entityFF.realFacingDirection == EnumFacing.DOWN ? -90.0F : 90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entityFF.realFacingDirection == EnumFacing.UP ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
        this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        IBakedModel ibakedmodel;

        if (!entityFF.getDisplayedItem().isEmpty() && entityFF.getDisplayedItem().getItem() == Items.FILLED_MAP)
        {
            ibakedmodel = modelmanager.getModel(this.mapModel);
        }
        else
        {
            ibakedmodel = modelmanager.getModel(this.itemFrameModel);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entityFF));
        }

        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.4375F);
        boolean flipItem = entityFF.realFacingDirection == EnumFacing.DOWN && !entityFF.getDisplayedItem().isEmpty() && entityFF.getDisplayedItem().getItem() instanceof ItemCompass;
        GlStateManager.rotate(flipItem ? -180.0F : 0.0F, 0.0F, 1.0F, 0.0F);
        this.renderItem(entityFF);
        GlStateManager.popMatrix();
        this.renderName(entityFF,
            x + (double)((float)entityFF.realFacingDirection.getFrontOffsetX() * 0.3F),
            y - (entityFF.realFacingDirection == EnumFacing.DOWN ? 1.25D : 0.25D),
            z + (double)((float)entityFF.realFacingDirection.getFrontOffsetZ() * 0.3F));
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(EntityItemFrame entity)
    {
        return null;
    }

    private void renderItem(EntityItemFrame itemFrame)
    {
        ItemStack itemstack = itemFrame.getDisplayedItem();

        if (!itemstack.isEmpty())
        {
            EntityItem entityitem = new EntityItem(itemFrame.world, 0.0D, 0.0D, 0.0D, itemstack);
            Item item = entityitem.getItem().getItem();
            entityitem.getItem().setCount(1);
            entityitem.hoverStart = 0.0F;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            int i = itemFrame.getRotation();

            if (item instanceof net.minecraft.item.ItemMap)
            {
                i = i % 4 * 2;
            }

            GlStateManager.rotate((float)i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);

            net.minecraftforge.client.event.RenderItemInFrameEvent event = new net.minecraftforge.client.event.RenderItemInFrameEvent(itemFrame, this);
            if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
            {
                if (item instanceof net.minecraft.item.ItemMap)
                {
                    this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                    float f = 0.0078125F;
                    GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
                    GlStateManager.translate(-64.0F, -64.0F, 0.0F);
                    MapData mapdata = Items.FILLED_MAP.getMapData(entityitem.getItem(), itemFrame.world);
                    GlStateManager.translate(0.0F, 0.0F, -1.0F);

                    if (mapdata != null)
                    {
                        this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
                    }
                }
                else
                {
                    GlStateManager.scale(0.5F, 0.5F, 0.5F);
                    GlStateManager.pushAttrib();
                    RenderHelper.enableStandardItemLighting();
                    this.itemRenderer.renderItem(entityitem.getItem(), ItemCameraTransforms.TransformType.FIXED);
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.popAttrib();
                }
            }

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void renderName(EntityItemFrame entity, double x, double y, double z)
    {
        if (Minecraft.isGuiEnabled() && !entity.getDisplayedItem().isEmpty() && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entity)
        {
            double d0 = entity.getDistanceSqToEntity(this.renderManager.renderViewEntity);
            float f = entity.isSneaking() ? 32.0F : 64.0F;

            if (d0 < (double)(f * f))
            {
                String s = entity.getDisplayedItem().getDisplayName();
                this.renderLivingLabel(entity, s, x, y, z, 64);
            }
        }
    }
}