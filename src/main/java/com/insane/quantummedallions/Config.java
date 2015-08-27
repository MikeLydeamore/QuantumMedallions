package com.insane.quantummedallions;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static String[] compressions;
	
	public static void doConfig(File file)
	{
		Configuration config = new Configuration(file);
		config.load();
		
		compressions = config.get("compression","toCompress", "", "Comma separated list of items to be 'compressable'. Format: modid:name:meta").getString().split(",");
		
		if (config.hasChanged())
			config.save();
	}

}
