package com.insane.quantummedallions;

import java.util.ArrayList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@Mod(modid=QuantumMedallions.MODID, name="QuantumMedallions", version="0.0.1")
public class QuantumMedallions {

	public static final String MODID = "QuantumMedallions";

	@Mod.Instance(MODID)
	public static QuantumMedallions QuantumMedallions;

	@SidedProxy(clientSide="com.insane.quantummedallions.client.ClientProxy", serverSide="com.insane.quantummedallions.CommonProxy")
	public static CommonProxy proxy;

	public static ItemQuantumCore quantumCore;
	
	public static BlockQuantumCreator quantumCreator;

	public static ArrayList<ItemStack> compressibles = new ArrayList<ItemStack>();
	public static ItemCompressible compressible;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		quantumCore = new ItemQuantumCore();
		GameRegistry.registerItem(quantumCore, quantumCore.getUnlocalizedName());

		GameRegistry.addShapedRecipe(new ItemStack(quantumCore, 32, 0), new Object[]{"xyx","yzy","xyx", 'x', Items.gold_ingot, 'y', Items.diamond, 'z', Items.nether_star});
		GameRegistry.addShapedRecipe(new ItemStack(quantumCore, 32, 0), new Object[]{"xyx","yzy","xyx", 'y', Items.gold_ingot, 'x', Items.diamond, 'z', Items.nether_star});
		GameRegistry.addShapedRecipe(new ItemStack(quantumCore, 1, 1), new Object[]{"xyx","yzy","xyx", 'y', Blocks.diamond_block, 'x', Blocks.gold_block, 'z', Items.nether_star});

		Config.doConfig(event.getSuggestedConfigurationFile());
		
		quantumCreator = new BlockQuantumCreator();
		GameRegistry.registerBlock(quantumCreator, quantumCreator.getUnlocalizedName());
		GameRegistry.registerTileEntity(TileEntityQuantumCreator.class, "tileQuantumCreator");
		
		GameRegistry.addShapedRecipe(new ItemStack(quantumCreator, 1, 0), new Object[] {"xyx","yzy","xyx", 'z', Blocks.chest, 'x', Items.diamond, 'y', Blocks.stone});
		
		proxy.registerRenderers();

	}



	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		PacketHandler.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		processCompressiblesIntoStacks();

		compressible = new ItemCompressible();
		GameRegistry.registerItem(compressible, compressible.getUnlocalizedName());

		proxy.registerRenderers();

		
		
		//Recipes
		ItemStack medallionStack = new ItemStack(quantumCore, 1, 0);
		ItemStack medallionStackNotConsumed = new ItemStack(quantumCore, 1, 1);
		for (int j = 0; j<getNumCompressibles(); j++)
		{
			if ((j+1)%8 != 0 || j == 0)
			{
				ItemStack outputStack = new ItemStack(compressible, 1, j+1);
				ItemStack inputStack = new ItemStack(compressible, 1, j);
				if ((j+1)%8 < 7)
					GameRegistry.addShapedRecipe(outputStack, new Object[] {"xxx", "xyx", "xxx", 'x', inputStack, 'y', medallionStackNotConsumed});
				else
					GameRegistry.addShapedRecipe(outputStack, new Object[] {"xxx","xyx","xxx", 'x', inputStack, 'y', medallionStack});
			}
		}
		
		for (int j = 0; j < getNumCompressibles()/8; j++)
		{
			ItemStack outputStack = new ItemStack(compressible, 1, j*8);
			GameRegistry.addShapedRecipe(outputStack, new Object[]{"xxx","xyx","xxx", 'x', compressibles.get(j), 'y', medallionStackNotConsumed});
		}
		
		//Reverse Recipes
		for (int j = 0; j < getNumCompressibles(); j++)
		{
			ItemStack inputStack = new ItemStack(compressible, 8, j);
			ItemStack outputStack;
			if (j%8 == 0)
			{
				outputStack = compressibles.get(j/8).copy();
				outputStack.stackSize=8;
			}
			else
			{
				outputStack = new ItemStack(compressible, 8, j-1);
			}
			
			GameRegistry.addShapelessRecipe(outputStack, inputStack);
		}
	}

	public static CreativeTabs tabQM = new CreativeTabs("QuantumMedallions")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return Items.nether_star;
		}
	};

	private void processCompressiblesIntoStacks() 
	{
		for (String s : Config.compressions)
		{
			String[] split = s.split(":");
			if (split.length!=3)
			{
				System.out.println("Error while processing "+split+". Not 3 terms!");
			}
			else
			{
				Item found = GameRegistry.findItem(split[0], split[1]);
				System.out.println(found.getUnlocalizedName()+":"+Integer.parseInt(split[2]));
				if(found != null)
				{
					ItemStack ss = new ItemStack(found, 1, Integer.parseInt(split[2]));
					compressibles.add(ss);
				}
				else if (GameRegistry.findBlock(split[0],split[1]) != null)
					compressibles.add(new ItemStack(GameRegistry.findBlock(split[0], split[1]), 1, Integer.getInteger(split[2])));
				else
					System.out.println("Error while processing "+split+". Cannot find Item/Block!");
			}
		}
	}

	public static int getNumCompressibles()
	{
		return compressibles.size()*8;
	}

}
