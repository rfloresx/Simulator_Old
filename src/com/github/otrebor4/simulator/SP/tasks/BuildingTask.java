package com.github.otrebor4.simulator.SP.tasks;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.github.otrebor4.simulator.SP.actinions.BlockInteraction;
import com.github.otrebor4.simulator.SP.actinions.Building;
import com.github.otrebor4.simulator.SP.actinions.WorldManipulationAction;
import com.github.otrebor4.simulator.SP.actinions.WorldManipulationAction.PLACE_BLOCK_ERR;
import com.github.otrebor4.simulator.properties.Structure;
import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.util.Construction;
import com.github.otrebor4.simulator.util.Construction.BlocksData;
import com.github.otrebor4.simulator.util.Timer;
import com.github.otrebor4.simulator.util.Vector3;

public class BuildingTask extends Task{
	Construction plan;
	WorldManipulationAction actions;
	World world;
	BlockInteraction interaction;
	CleanTask clean;
	Timer timer = new Timer();
	Building build;
	boolean completeClean = false;
	
	public BuildingTask(CraftSP npc) {
		super(npc);
		actions = new WorldManipulationAction(npc);
		world = npc.getBukkitEntity().getWorld();
		plan = Structure.getConstruction();
		Location temp = npc.getBukkitEntity().getLocation();
		plan.SetWordPos( new Vector3(temp.getBlockX(), temp.getBlockY()-1, temp.getBlockZ() ));
		clean = new CleanTask(npc);
		Vector3 offset = new Vector3(6, 2, 6);
		clean.setZone2( plan.getCenterXZ(), plan.getDimension().add(offset));
		clean.finished = false;
	}
	
	@Override
	public void Update(){
		
		
		if(clean != null ){
			if( !completeClean && !CleanTask.isZoneClean( clean.getPos1(), clean.getPos2() , world)){
				clean.Update();
				if(CleanTask.isZoneClean( clean.getPos1(), clean.getPos2() , world)){
					completeClean = true;
				}
				return;
			}
			else if(!clean.done()){
				clean.Update();
				return;
			}
			else if(!clean.Done2()){
				clean.Update2();
				return;
			}
		}
		
		if( build != null){
			build.Update();
			if(build.done()){
				BlocksData b = plan.getBlockPos(build.getLoc());
				if(b != null && b.blockid == 64 ){
					Vector3 [] places = actions.placeDoor( plan.getWorldPos( b.pos)  , b.blockid);
					boolean flag = false;
					if( places != null){
						for(int i = 0; i < places.length; i++){
							if( places[i] != null){
								clean.addTarget( places[i]);
								flag = true;
							}
						}
					}
					if( !flag){
						plan.remobeBlockPos(build.getLoc());
					}
					
				}
				else if(b != null && actions.placeBlock( b.blockid, plan.getWorldPos( b.pos) ) != PLACE_BLOCK_ERR.NONE){
					Vector3 temp = plan.getWorldPos( b.pos);
					Block bl = world.getBlockAt( temp.x, temp.y, temp.z);
					if( bl != null && bl.getTypeId() != b.blockid )
						clean.addTarget(plan.getWorldPos( b.pos));
					else{
						plan.remobeBlockPos(build.getLoc());
					}
					//plan.addBlock(b.pos, b.blockid);
				}
				else if( b != null){
					plan.remobeBlockPos(build.getLoc());
				}
			}
			else if(!build.isInAction()){
				BlocksData b = plan.remobeBlockPos(build.getLoc());
				if(b != null ){
					plan.addBlock(b.pos, b.blockid);
				}
			}
			else{
				return;
			}
			
		}
		
		Vector3 pos = plan.getNextPos();
		if(pos != null){
			int id = plan.getBlockIdPos(pos);
			if(id != -1){
				Vector3 pos1 = plan.getWorldPos(pos);
				Location targ = new Location(m_NPC.getBukkitEntity().getWorld(), pos1.x, pos1.y, pos1.z);
				build = new Building(this.m_NPC,  targ, pos);
			}
		}
		else{
			this.finished = true;
		}
	}
	
	

}
