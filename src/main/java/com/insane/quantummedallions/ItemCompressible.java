package com.insane.quantummedallions;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCompressible extends ItemQuantum {

	public ItemCompressible()
	{
		super();
		this.setUnlocalizedName("quantumCompressible");
		this.setHasSubtypes(true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for (int i=0; i<QuantumMedallions.getNumCompressibles(); i++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int meta)
	{
		ItemStack stack = QuantumMedallions.compressibles.get(meta/8);
		return stack.getItem().getIconFromDamage(stack.getItemDamage());
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		int meta = stack.getItemDamage();
		ItemStack originalStack = QuantumMedallions.compressibles.get(meta/8);
		String s = originalStack.getItem().getUnlocalizedNameInefficiently(originalStack);

		String compression;
		//Level of compression
		switch (meta%8)
		{
		case 0:
			compression = "compressed";
			break;
		case 1:
			compression = "doublecompressed";
			break;
		case 2:
			compression = "triplecompressed";
			break;
		case 3:
			compression = "quadruplecompressed";
			break;
		case 4:
			compression = "quintuplecompressed";
			break;
		case 5:
			compression = "sextuplecompressed";
			break;
		case 6:
			compression = "septuplecompressed";
			break;
		case 7:
			compression = "octuplecompressed";
			break;
		default:
			compression = "";
			break;

		}
		return s == null ? "" : StatCollector.translateToLocal(compression)+" "+ originalStack.getDisplayName();
	}

}
