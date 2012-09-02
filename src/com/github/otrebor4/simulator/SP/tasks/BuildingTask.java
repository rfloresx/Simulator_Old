package com.github.otrebor4.simulator.SP.tasks;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.PlayerInventory;

import com.github.otrebor4.simulator.SP.actinions.BlockInteraction;
import com.github.otrebor4.simulator.SP.actinions.Building;
import com.github.otrebor4.simulator.SP.actinions.WorldManipulationAction;
import com.github.otrebor4.simulator.SP.actinions.WorldManipulationAction.PLACE_BLOCK_ERR;
import com.github.otrebor4.simulator.properties.Structure;
import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.util.Construction;
import com.github.otrebor4.simulator.util.Construction.BlocksData;
import com.github.otrebor4.simulator.util.Messaging;
import com.github.otrebor4.simulator.util.Timer;
import com.github.otrebor4.simulator.util.Vector3;

public class BuildingTask extends Task{
	Construction plan;
	WorldManipulationAction actions;
	World world;
	BlockInteraction interaction;
	CollectTask supply = null;
	CleanTask clean;
	Timer timer = new Timer();
	Building build;
	HashMap<Integer, Integer> needs;
	
	boolean completeClean = false;
	boolean needMaterial = true;
	boolean firstCheck = true;
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
		materialCheck();
		if(needMaterial ){
			Messaging.log("needMaterial");
			if(supply == null){
				supply = new CollectTask(m_NPC, needs );
			}
			else{
				supply.Update();
				if(supply.done()){
					needMaterial = false;
					supply = null;
				}
			}
			return;
		}
		//clean the zone to build
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
				this.placeBlock();
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
	
	private void placeBlock(){
		BlocksData b = plan.getBlockPos(build.getLoc());
		
		//door use two blocks
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
	
	private void materialCheck(){
		if(firstCheck){
			if(needMaterial()){
				if(plan != null){
					List<Integer> keys = new LinkedList<Integer>();
					needs = plan.getMaterial();
					for( Integer key : needs.keySet()){
						if(haveItem(key,needs.get(key))){
							keys.add(key);
						}
					}
					for(Integer key : keys){
						if(needs.containsKey(key)){
							needs.remove(key);
						}
					}
				}
			}
		}
		firstCheck = false;
		
	}
	
	private boolean needMaterial(){
		if(plan != null){
			HashMap<Integer, Integer> list = plan.getMaterial();
			for( Integer key : list.keySet()){
				if(!haveItem(key,list.get(key))){
					Messaging.log("need Materials");
					return true;
				}
			}
		}
		
		
		return false;
	}
	
	public boolean haveItem(int id, int count){
		if(m_NPC != null){
			PlayerInventory inv = this.m_NPC.getBukkitEntity().getInventory();
			return inv.contains(id, count);
		}
		return false;
	}

}
