package com.github.otrebor4.simulator;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.otrebor4.simulator.SP.SimulatedPlayerManager;
import com.github.otrebor4.simulator.events.SimulatorListener;
import com.github.otrebor4.simulator.util.Messaging;

public class Simulator extends JavaPlugin {
	public static Simulator plugin;
	
	@Override
	public void onEnable(){ 
		plugin = this;
		 Settings.setupVariables();
		 SPSettings.setupVariables();
		 getServer().getPluginManager().registerEvents( new SimulatorListener(), this);
		 getServer().getScheduler().scheduleSyncRepeatingTask(this,new SimulatedPlayerManager() , 0, 1);
		 
		 Messaging.log( "Test" );
	}
	 
	@Override
	public void onDisable(){ 
	 
	}
	
}
