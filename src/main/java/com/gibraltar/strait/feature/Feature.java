/**
 * This class was created by Rock Hymas. It's distributed as
 * part of the Strait Mod. Get the Source Code in github:
 * https://github.com/rockhymas/strait
 *
 * Strait is Open Source and distributed under the
 * CC-BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package com.gibraltar.strait.feature;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.FMLLog;

public class Feature {
	public boolean enabled;
    public String name;

    public Feature() {
        name = getClass().getSimpleName().replaceAll("Feature", "").toLowerCase();
    }

	public void preInit(FMLPreInitializationEvent event) {
		FMLLog.info("pre init");
		if (hasSubscriptions()) {
			FMLLog.info("subscribing!");
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	public void preInitClient(FMLPreInitializationEvent event) {
	}

	public void init(FMLInitializationEvent event) {
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	protected boolean hasSubscriptions() {
		return false;
	}

	public void loadConfig(Configuration config) {
        Property prop = config.get("_features", name, true);
        enabled = prop.getBoolean(true);
	}
}