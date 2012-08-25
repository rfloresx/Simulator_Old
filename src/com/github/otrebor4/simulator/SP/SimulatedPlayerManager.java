package com.github.otrebor4.simulator.SP;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDeathEvent;

import com.github.otrebor4.simulator.Settings;
import com.github.otrebor4.simulator.properties.settings.BasicProperties;
import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.resources.SimulatorNPC;
import com.github.otrebor4.simulator.util.Messaging;
import com.google.common.collect.MapMaker;

public class SimulatedPlayerManager implements Runnable {
	private final static Map<Integer, SimulatorNPC> simulator = new MapMaker().makeMap();
	
	private final static List<SimulatorNPC> spawner = new LinkedList<SimulatorNPC>();
	
	private boolean load = true;
	static int delay = 0;
	
	@Override
	public void run() {
		if(load){
			loadSimulator();
			load = false;
		}
		if(!spawner.isEmpty() && delay <= 0){
			SimulatorNPC npc = spawner.remove(0);
			spawnSimulator( npc);
			npc.resotreValues();
			if(!spawner.isEmpty())
				delay = 25;
		}
		
		for(SimulatorNPC SP : simulator.values()){
			SP.doTick();
		}
		if(delay > 0)
			delay--;
	}
	
	private void loadSimulator(){
		String [] names = Settings.getString("Names").split(",");
		int count = 1;
		for(String name : names) {
			 SimulatedPlayer SP = BasicProperties.loadState(name);
			 spawnSimulator( SP, SP.getLocation(), count);
			 count++;
		}
		
	}
	
	private static void spawnSimulator( SimulatedPlayer SP, Location location, int id ){
		SimulatorNPC SPNPC = Spawner.spawnNPC(location, SP, id);
		simulator.put( SPNPC.getPlayer().getEntityId(), SPNPC);
		Messaging.log("spawning " + SPNPC.getName() + " in location " + location.toString());
	}
	
	private static void spawnSimulator(SimulatorNPC SP){
		Location dest = SP.getSpawnPoint();
        spawnSimulator(SP.getHandleSP(), dest, SP.getPlayer().getEntityId()  );
	}
	
	public static void reSpawn(SimulatorNPC npc) {
        if (simulator.get(npc.getPlayer().getEntityId()) == null)
            return;
        SimulatorNPC SP = simulator.remove(npc.getPlayer().getEntityId());
        Spawner.despawnNPC( SP );
  
        spawner.add(SP);
        if(delay <= 0)
        	delay = 25;
    }
	
	public static SimulatorNPC get(Entity entity) {
		if(entity == null)
			return null;
		net.minecraft.server.Entity mcEntity = ((CraftEntity) entity)
				.getHandle();
		if (mcEntity instanceof CraftSP) {
			SimulatorNPC npc = ((CraftSP) mcEntity).npc;
			if (npc == null)
				return null;
			if (simulator.get(npc.getPlayer().getEntityId()) == npc) {
				return npc;
			}
		}
		
		return null;
	}

	public void callDeathEvent(EntityDeathEvent event) {
		SimulatorNPC npc = SimulatedPlayerManager.get(event.getEntity());
		reSpawn(npc);
		simulator.get( npc.getPlayer().getEntityId()).resotreValues();
	}
	
	
}
