package com.github.otrebor4.simulator.SP.tasks;

import java.util.LinkedList;
import java.util.List;

import com.github.otrebor4.simulator.SP.actinions.Action;
import com.github.otrebor4.simulator.SP.actinions.SafeWalk;
import com.github.otrebor4.simulator.SP.actinions.Walk;
import com.github.otrebor4.simulator.resources.PathNPC;
import com.github.otrebor4.simulator.util.Vector2;

public class SafeTask extends Task {
	int stage = 0;
	public SafeTask(PathNPC npc) {
		super(npc);
		this.action = getAction(npc);
		this.priority = 3;
	}
	
	public List<Action> getAction( PathNPC npc){
		List<Action> act = new LinkedList<Action>();
		act.add( getActionPriority() );
		act.add(new Walk(m_NPC,  Vector2.getRVRange( -1, 1)) );
		return act;
	}
	
	public Action getActionPriority(){
		if(m_NPC.getBukkitEntity().getHealth() <= m_NPC.getBukkitEntity().getMaxHealth()/4 ){
			return new SafeWalk(m_NPC);
		}
		return null;
	}
	
	@Override   
	public void Update(){
		//super.Update();
		if((action == null || action.isEmpty() )|| action.get(0) == null){
			this.finished = true;
			return;
		}
		
		if( action.get(0).done() && PathNPC.getMonsterIn(m_NPC, 20) != null && PathNPC.getMonsterIn(m_NPC, 20).isEmpty()){
			stage = 1;
		}
		else{stage = 0; action.get(0).setDone(false);}
		
		action.get(stage).Update();
		
		if(m_NPC.getBukkitEntity().getHealth() > m_NPC.getBukkitEntity().getMaxHealth()/4 ){
			action.clear();
		}
		
	}
	
	
}
