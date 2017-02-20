/**
 * This class was created by Rock Hymas. It's distributed as
 * part of the Strait Mod. Get the Source Code in github:
 * https://github.com/rockhymas/strait
 *
 * Strait is Open Source and distributed under the
 * CC-BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package com.gibraltar.strait.proxy;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.gibraltar.strait.feature.Feature;
import com.gibraltar.strait.feature.FrameFeature;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class CommonProxy
{
	public static Configuration config;
	public static File configFile;
    protected ArrayList features;

    public void preInit(FMLPreInitializationEvent event)
    {
        configFile = event.getSuggestedConfigurationFile();
        System.out.println(configFile);
		config = new Configuration(configFile);
		config.load();

        features = new ArrayList();
        features.add(new FrameFeature());

        forEachFeature(feature -> feature.loadConfig(config));

        if (config.hasChanged())
        {
            config.save();
        }

        forEachFeature(feature ->
        {
            if (feature.enabled)
            {
                feature.preInit(event);
            }
        });

        config.save();
    }

	public void init(FMLInitializationEvent event)
    {
        forEachFeature(feature -> {
            if (feature.enabled) {
                feature.init(event);
            }
        });
    }

    protected void forEachFeature(Consumer<Feature> action)
    {
	    features.forEach(action);
    }

    public boolean isFeatureEnabled(Class clazz)
    {
        boolean enabled = false;
        for (int i = 0; i < features.size(); i++)
        {
            Feature feature = (Feature)features.get(i);
            if (clazz.isInstance(feature))
            {
                enabled = feature.enabled;
            }
        }

        return enabled;
    }
}
