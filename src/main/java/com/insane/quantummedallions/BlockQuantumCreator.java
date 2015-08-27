package com.insane.quantummedallions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockQuantumCreator extends BlockContainer {

	public BlockQuantumCreator()
	{
		super(Material.ground);
		this.setBlockName("quantumCreator");
		this.setCreativeTab(QuantumMedallions.tabQM);
		this.setBlockBounds(0.1f, 0f, 0.1f, 0.9f, 0.5f, 0.9f);
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return Blocks.stone.getIcon(side,  meta);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityQuantumCreator();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitz)
	{
		if (!world.isRemote)
		{
			TileEntityQuantumCreator tile = (TileEntityQuantumCreator) world.getTileEntity(x, y, z);
			ItemStack held = player.getHeldItem();

			if (held != null && held.getItem() instanceof ItemCompressible && ((held.getItemDamage()+1) % 8) == 0 && tile.getStackInSlot(tile.itemSlot) == null )
			{
				ItemStack set = held.copy();
				set.stackSize=1;
				tile.setInventorySlotContents(tile.itemSlot, set);
				tile.markDirty();
				world.markBlockForUpdate(x, y, z);

				held.stackSize--;

				return true;
			}
			
			
			if (player.isSneaking() && tile.getStackInSlot(tile.itemSlot) != null)
			{

				EntityItem item = new EntityItem(world, x, y, z, tile.getStackInSlot(tile.itemSlot));
				world.spawnEntityInWorld(item);
				
				tile.setInventorySlotContents(tile.itemSlot, null);
				tile.markDirty();
				world.markBlockForUpdate(x, y, z);
				
				return true;
			}
		}
		return false;
	}


}
