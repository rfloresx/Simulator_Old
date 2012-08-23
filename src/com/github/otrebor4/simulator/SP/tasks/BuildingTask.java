package com.github.otrebor4.simulator.SP.tasks;

import org.bukkit.World;

import com.github.otrebor4.simulator.SP.actinions.BlockInteraction;
import com.github.otrebor4.simulator.SP.actinions.WorldManipulationAction;
import com.github.otrebor4.simulator.resources.PathNPC;

public class BuildingTask extends Task{
	WorldManipulationAction actions;
	World world;
	BlockInteraction interaction;
	
	public BuildingTask(PathNPC npc) {
		super(npc);
		actions = new WorldManipulationAction(npc);
		world = npc.getBukkitEntity().getWorld();
		
	}
	
	
	
	

}
