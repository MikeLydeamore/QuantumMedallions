package com.insane.quantummedallions;


import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(QuantumMedallions.MODID);
	private static int id = 0;
	
	public static void init()
	{
		INSTANCE.registerMessage(MessageStackUpdate.class, MessageStackUpdate.class, id++, Side.CLIENT);
	}

}
