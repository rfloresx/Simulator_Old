package com.github.otrebor4.simulator.SP.actinions;

import org.bukkit.Location;

import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.util.Vector3;


public class Building extends Action{
	static float MIN_DIST = 5f;
	Location targ;
	boolean stationary = false;
	Vector3 loc;
	public Building(CraftSP npc, Location target, Vector3 loc) {
		super(npc);
		this.targ = target;
		this.loc = loc;
	}
	
	@Override
	public void Update(){
		//Messaging.log("Building Update");
		double dis = targ.distance(npc.getBukkitEntity().getLocation());
		if( dis < MIN_DIST ){
			if( npc.IsFacingT( targ )){
				//Messaging.log("Update Done");
				this.finished = true;
				
			}else{
				npc.faceTo( targ);
			}
		}
		else{
			if(npc.MoveToPos( targ.getBlockX(), targ.getBlockY(), targ.getBlockZ())){
				stationary = true;
			}
		}
	}

	@Override
	public boolean isInAction(){
		return !stationary;
	}
	
	public Vector3 getTarget(){
		if(targ != null)
			return new Vector3(targ.getBlockX(), targ.getBlockY(), targ.getBlockZ());
		return null;
	}
	
	public Vector3 getLoc(){return loc;}

}
