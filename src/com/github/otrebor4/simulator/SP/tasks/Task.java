package com.github.otrebor4.simulator.SP.tasks;

import java.util.LinkedList;
import java.util.List;

import com.github.otrebor4.simulator.SP.actinions.Action;
import com.github.otrebor4.simulator.resources.PathNPC;


public class Task {
	PathNPC m_NPC;
	boolean finished = false;
	protected List<Action> action = new LinkedList<Action>();
	int priority = 0;
	//protected Map<String, Action > action =  new MapMaker().makeMap();
	
	public Task( PathNPC npc){
		finished = false;
		m_NPC = npc;
	}
	
	public void Update(){
		if(action != null && !action.isEmpty() && action.get(0) == null){
			action.remove(0);
		}
		
		if(action == null || action.isEmpty()){
			finished = true;
			return;
		}
		
		action.get(0).Update();
		
		if(action.get(0).done()){
			//Messaging.log( "Action done" + action.get(0).toString());
			action.remove(0);
		}
	}
	
	public boolean done(){
		return finished;
	}

	public boolean isHightPriority(int val){
		return val <= priority;
	}
}
