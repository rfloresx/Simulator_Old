package com.github.otrebor4.simulator.SP.tasks;

import java.util.LinkedList;
import java.util.List;

import com.github.otrebor4.simulator.SP.actinions.Action;
import com.github.otrebor4.simulator.SP.actinions.CollectItem;
import com.github.otrebor4.simulator.resources.CraftSP;

public class CollectTask extends Task{

	public CollectTask(CraftSP npc) {
		super(npc);
		action = getActions(npc);
	}
	
	@Override
	public void Update(){
		super.Update();
	}
	
	public List<Action> getActions( CraftSP npc){
		List<Action> actions = new LinkedList<Action>();
		actions.add( new CollectItem(npc,10));
		return actions;
	}
}
