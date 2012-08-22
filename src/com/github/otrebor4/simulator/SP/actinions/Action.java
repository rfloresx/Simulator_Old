package com.github.otrebor4.simulator.SP.actinions;

import com.github.otrebor4.simulator.resources.PathNPC;


public class Action {
	
	PathNPC npc;
	float ActionTime = 0;
	boolean finished = false;
	
	public Action(PathNPC npc){
		this.npc = npc;
		
	}
	
	public void Update(){
		
		
	}
	
	public boolean done(){ return finished;}

	public void setDone(boolean val){ finished = val;}
}
