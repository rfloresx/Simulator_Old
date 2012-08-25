package com.github.otrebor4.simulator.SP.tasks;


import org.bukkit.Location;
import org.bukkit.World;

import com.github.otrebor4.simulator.SP.actinions.BlockInteraction;
import com.github.otrebor4.simulator.SP.actinions.WorldManipulationAction;
import com.github.otrebor4.simulator.properties.Structure;
import com.github.otrebor4.simulator.resources.PathNPC;
import com.github.otrebor4.simulator.util.Construction;
import com.github.otrebor4.simulator.util.Messaging;
import com.github.otrebor4.simulator.util.Vector3;

public class BuildingTask extends Task{
	Construction plan;
	WorldManipulationAction actions;
	World world;
	BlockInteraction interaction;
	
	public BuildingTask(PathNPC npc) {
		super(npc);
		actions = new WorldManipulationAction(npc);
		world = npc.getBukkitEntity().getWorld();
		plan = Structure.getConstruction();
		Location temp = npc.getBukkitEntity().getLocation();
		plan.SetWordPos( new Vector3(temp.getBlockX(), temp.getBlockY(), temp.getBlockZ() ));
	}
	@Override
	public void Update(){
		Vector3 pos = plan.getNextPos();
		if(pos != null){
			
			int id = plan.getBlockIdPos(pos);
			Messaging.log(" pos " + plan.getWorldPos(pos).toString() + " blockid " + id);
			if(id != -1){
				 actions.placeBlock(id, plan.getWorldPos(pos));
				plan.remobeBlockPos( pos );
			}
		}
		else{
			this.finished = true;
		}
	}
	
	

}
