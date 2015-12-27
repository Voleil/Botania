/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 25, 2014, 9:42:31 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.Color;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelSpreader;
import vazkii.botania.common.block.tile.mana.TileSpreader;

public class RenderTileSpreader extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_SPREADER);
	private static final ResourceLocation textureRs = new ResourceLocation(LibResources.MODEL_SPREADER_REDSTONE);
	private static final ResourceLocation textureDw = new ResourceLocation(LibResources.MODEL_SPREADER_DREAMWOOD);

	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_HALLOWEEN);
	private static final ResourceLocation textureRsHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_REDSTONE_HALLOWEEN);
	private static final ResourceLocation textureDwHalloween = new ResourceLocation(LibResources.MODEL_SPREADER_DREAMWOOD_HALLOWEEN);

	private static final ModelSpreader model = new ModelSpreader();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float ticks, int digProgress) {
		TileSpreader spreader = (TileSpreader) tileentity;
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);

		GlStateManager.translate(0.5F, 1.5F, 0.5F);
		GlStateManager.rotate(spreader.rotationX + 90F, 0F, 1F, 0F);
		GlStateManager.translate(0F, -1F, 0F);
		GlStateManager.rotate(spreader.rotationY, 1F, 0F, 0F);
		GlStateManager.translate(0F, 1F, 0F);

		ResourceLocation r = spreader.isRedstone() ? textureRs : spreader.isDreamwood() ? textureDw : texture;
		if(ClientProxy.dootDoot)
			r = spreader.isRedstone() ? textureRsHalloween : spreader.isDreamwood() ? textureDwHalloween : textureHalloween;

		Minecraft.getMinecraft().renderEngine.bindTexture(r);
		GlStateManager.scale(1F, -1F, -1F);

		double time = ClientTickHandler.ticksInGame + ticks;

		if(spreader.isULTRA_SPREADER()) {
			Color color = Color.getHSBColor((float) ((time * 5 + new Random(spreader.getPos().hashCode()).nextInt(10000)) % 360) / 360F, 0.4F, 0.9F);
			GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
		}
		model.render();
		GlStateManager.color(1F, 1F, 1F);

		GlStateManager.pushMatrix();
		double worldTicks = tileentity.getWorld() == null ? 0 : time;
		GlStateManager.rotate((float) worldTicks % 360, 0F, 1F, 0F);
		GlStateManager.translate(0F, (float) Math.sin(worldTicks / 20.0) * 0.05F, 0F);
		model.renderCube();
		GlStateManager.popMatrix();
		GlStateManager.scale(1F, -1F, -1F);
		ItemStack stack = spreader.getStackInSlot(0);

		if(stack != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			ILens lens = (ILens) stack.getItem();
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.0F, -1F, -0.4375F);
			GlStateManager.scale(0.8F, 0.8F, 0.8F);
			Minecraft.getMinecraft().getRenderItem().func_181564_a(stack, ItemCameraTransforms.TransformType.FIXED);
			GlStateManager.popMatrix();
		}

		if(spreader.paddingColor != -1) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

			Block block = Blocks.carpet;
			int color = spreader.paddingColor;
			//RenderBlocks render = RenderBlocks.getInstance();
			float f = 1F / 16F;
			GlStateManager.translate(0F, -f, 0F);
			//render.renderBlockAsItem(block, color, 1F);
			GlStateManager.translate(0F, -f * 15, 0F);
			//render.renderBlockAsItem(block, color, 1F);
			GlStateManager.rotate(90F, 1F, 0F, 0F);
			GlStateManager.rotate(90F, 0F, 1F, 0F);

			GlStateManager.pushMatrix();
			GlStateManager.scale(f * 14F, 1F, 1F);
			//render.renderBlockAsItem(block, color, 1F);
			GlStateManager.popMatrix();

			GlStateManager.rotate(90F, 1F, 0F, 0F);
			GlStateManager.translate(0F, 0F, -f / 2);
			GlStateManager.scale(f * 14F, 1F, f * 15F);
			//render.renderBlockAsItem(block, color, 1F);
			GlStateManager.translate(0F, f * 15F, 0F);
			//render.renderBlockAsItem(block, color, 1F); todo 1.8
		}

		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

}
