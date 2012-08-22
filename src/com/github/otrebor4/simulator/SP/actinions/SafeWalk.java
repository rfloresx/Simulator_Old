package com.github.otrebor4.simulator.SP.actinions;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Monster;

import com.github.otrebor4.simulator.resources.PathNPC;
import com.github.otrebor4.simulator.util.Vector2;

public class SafeWalk extends Action{
	public SafeWalk(PathNPC npc) {
		super(npc);
	}

	@Override
	public void Update(){
		Vector2 dest = getSafeDir();
		if(dest == null){
			this.finished = true;
			npc.setRun( false );
			return;
		}
		
		if(!npc.isSprinting())
			npc.setRun(true);
		this.npc.MoveToDirection( dest.x, dest.y);
	}
	
	@Override
	public String toString(){
		return "Action SafeWalk";
	}
	
	private Vector2 getSafeDir(){
		Collection<Monster> list = this.npc.getBukkitEntity().getWorld().getEntitiesByClass( Monster.class );
		//List<Monster> m_list = new LinkedList<Monster>();
		Vector2 m_pos = new Vector2(this.npc.getBukkitEntity().getLocation().getBlockX(), this.npc.getBukkitEntity().getLocation().getBlockZ());
		Vector2 v = null;
		if( list != null &&  list.size() > 0)
			v = new Vector2(0,0);
		
		for( Monster m : list){
			if(m.getLocation().distance( this.npc.getBukkitEntity().getLocation()) < 30){
				if(m_pos.x > m.getLocation().getBlockX()){
					v.x ++;
				}
				else{v.x--;}
				if(m_pos.y > m.getLocation().getBlockZ()){
					v.y++;
				}else{v.y--;}
			}
		}
		//Vector2 v = new Vector2(0,0);
		if(v != null){
			if(v.x < 0){v.x = -1;}
			else if(v.x>0){v.x = 1;}
			
			if(v.y < 0){v.y = -1;}
			else if(v.y>0){v.y = 1;}
			
			if( v.x == 0 && v.y == 0){
				v = null;
			}
		}
		
		return v;
	}
	
	public Monster getClosestMonster(Location loc, Collection<Monster> Mobs){
		Monster clossest = null;
		double dif = 1000;
		for( Monster m : Mobs ){
			double diff = loc.distance( m.getLocation());
			if( diff < dif ){
				dif = diff;
				clossest = m;
			}
		}
			
		return clossest;
	}
	
	
	
}
