package com.github.otrebor4.simulator.SP.actinions;

import com.github.otrebor4.simulator.resources.CraftSP;




public class Action {
	
	CraftSP npc;
	float ActionTime = 0;
	boolean finished = false;
	
	public Action(CraftSP npc){
		this.npc = npc;
		
	}
	
	public void Update(){
		
		
	}
	
	public boolean done(){ return finished;}

	public void setDone(boolean val){ finished = val;}
}
