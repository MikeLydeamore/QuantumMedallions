package com.insane.quantummedallions;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemQuantumCore extends ItemQuantum {
	
	public ItemQuantumCore()
	{
		super();
		this.setUnlocalizedName("quantumCore");
		this.setHasSubtypes(true);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int i=0; i<2; i++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.getUnlocalizedName()+"."+stack.getItemDamage();
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return stack.getItemDamage()==1;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		if (hasContainerItem(stack))
		{
			ItemStack returnStack = stack.copy();
			returnStack.stackSize=1;
			return returnStack;
		}
	
		return null;
	}
	
	@Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_)
    {
        return true;
    }

}
