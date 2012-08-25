package com.github.otrebor4.simulator.SP;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import com.github.otrebor4.simulator.SP.tasks.BuildingTask;
import com.github.otrebor4.simulator.SP.tasks.CleanTask;
import com.github.otrebor4.simulator.SP.tasks.CollectTask;
import com.github.otrebor4.simulator.SP.tasks.ExploreTask;
import com.github.otrebor4.simulator.SP.tasks.SafeTask;
import com.github.otrebor4.simulator.SP.tasks.Task;



public class TasksManager {
	enum TaskType{ 
		EXPLORE,
		SAFE,
		CLEAN,
		COLLECT,
		BUILDING;
	}
	
	static SimulatedPlayer m_player;
	
	public TasksManager(SimulatedPlayer SP){
		m_player = SP;
	}
	
	public Task getTask( ){
		TaskType TT = getTaskType();
		switch (TT){
		case EXPLORE:
			return new ExploreTask(m_player);
		case SAFE:
			return new SafeTask(m_player);
		case CLEAN:
			return new CleanTask(m_player);
		case COLLECT:
			return new CollectTask(m_player);
		case BUILDING:
			return new BuildingTask(m_player);
		}
		return null;
	}
	
	public TaskType getTaskType(){
		List< TaskType> types = new LinkedList< TaskType>();
		for(TaskType TT: EnumSet.allOf( TaskType.class) ){
			types.add(TT);
		}
		TaskType temp = getTypePriority(types);
		if(temp != null){
			return temp;
		}
		
		//Random rand = Rand.random;
		if( !types.isEmpty())
			return types.get( 4); //rand.nextInt(types.size()));
		return null;
	}
	
	public TaskType getTypePriority( List< TaskType> types ){
		if(m_player.getBukkitEntity().getHealth() < m_player.getBukkitEntity().getMaxHealth()/3){
			return TaskType.SAFE;
		}
		return null;
	}
}
