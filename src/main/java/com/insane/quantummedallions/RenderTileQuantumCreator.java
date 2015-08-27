package com.insane.quantummedallions;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class RenderTileQuantumCreator extends TileEntitySpecialRenderer {

	public static EntityItem item;
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) 
	{
		if (item==null)
			item = new EntityItem(tile.getWorldObj());
		
		GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);
        
        TileEntityQuantumCreator te = (TileEntityQuantumCreator) tile;
        ItemStack stack = te.getStackToCreate();
        //System.out.println(stack == null ? "NULL" : stack.getDisplayName());
        item.setEntityItemStack(stack);
        
        RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, 0.8D, 0.0D, 0.0F, 0.0F);
        GL11.glPopMatrix();
		
	}

}
