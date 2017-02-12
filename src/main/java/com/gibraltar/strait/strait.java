/**
 * This class was created by Rock Hymas. It's distributed as
 * part of the Strait Mod. Get the Source Code in github:
 * https://github.com/rockhymas/strait
 *
 * strait is Open Source and distributed under the
 * CC-BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package com.gibraltar.strait;

import com.gibraltar.strait.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.MOD_NAME/*, guiFactory = Reference.GUI_FACTORY*/)
public class strait
{
    @Instance(Reference.MODID)
	public static strait instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
