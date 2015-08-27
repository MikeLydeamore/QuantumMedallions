package com.insane.quantummedallions.client;

import com.insane.quantummedallions.CommonProxy;
import com.insane.quantummedallions.RenderTileQuantumCreator;
import com.insane.quantummedallions.TileEntityQuantumCreator;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	
	public void registerRenderers()
	{
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuantumCreator.class, new RenderTileQuantumCreator());
	}

}
