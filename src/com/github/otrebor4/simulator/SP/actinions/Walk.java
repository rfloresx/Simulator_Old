package com.github.otrebor4.simulator.SP.actinions;



import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.util.Vector2;


public class Walk extends Action {
	Vector2 dest;
	
	public Walk(CraftSP npc, Vector2 V2) {
		super(npc);
		this.dest = V2;
		//this.ActionTime = Rand.random.nextInt()%1000 + 1000;
	}

	@Override
	public void Update(){
		this.npc.MoveToDirection(dest.x, dest.y);
	}
	
	@Override
	public String toString(){
		return "Action Walk (" + dest.x + "," + dest.y + ")";
	}

}
