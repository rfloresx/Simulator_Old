package com.github.otrebor4.simulator.SP.tasks;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.github.otrebor4.simulator.SP.actinions.Action;
import com.github.otrebor4.simulator.SP.actinions.Walk;
import com.github.otrebor4.simulator.resources.PathNPC;
import com.github.otrebor4.simulator.util.Messaging;
import com.github.otrebor4.simulator.util.Rand;
import com.github.otrebor4.simulator.util.Vector2;


public class ExploreTask extends Task{
	double TaskTime;
	
	public ExploreTask( PathNPC npc ){
		super(npc);
		this.action = getAction( npc );
		TaskTime = Rand.random.nextInt(2500) + 1000;
	}
	
	public List<Action> getAction( PathNPC npc){
		List<Action> act = new LinkedList<Action>();
		Random rand = Rand.random;
		int x = -2;
		int y = -2;
		while( x < -1 || x > 1){
			x = rand.nextInt(3)-1;
		}while( y < -1 || y > 1){
			y= rand.nextInt(3)-1;
		}
		
		
		Walk a = new Walk( npc,new Vector2(x,y));
		Messaging.log( "adding action " + a.toString());
		act.add(a);
		
	
		return act;
	}

	@Override   
	public void Update(){
		super.Update();	 
 
		TaskTime -=  1;
		if(TaskTime <= 0){
			action.clear();
		}
		
	}
}
