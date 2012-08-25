package com.github.otrebor4.simulator.SP.tasks;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.github.otrebor4.simulator.SP.actinions.BlockInteraction;
import com.github.otrebor4.simulator.SP.actinions.WorldManipulationAction;
import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.util.Messaging;
import com.github.otrebor4.simulator.util.Vector3;

public class CleanTask extends Task {
	WorldManipulationAction actions;
	private Vector3 pos1, pos2;
	private World world;
	private Vector3 target;
	BlockInteraction interaction;
	List<Vector3> targets = new LinkedList<Vector3>();
	
	private Vector3 curPos = Vector3.ZERO();
	
	//TODO: dynamically set the dimension of the zone to clean
	public CleanTask(CraftSP npc) {
		super(npc);
		actions = new WorldManipulationAction(npc);
		world = npc.getBukkitEntity().getWorld();
		setZone( geVectorFromLocation(npc.getBukkitEntity().getLocation()), new Vector3( 10, 10, 10)  );
	}
	 
	public Vector3 geVectorFromLocation( Location l){
		return new Vector3(l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}
	
	void setZone( Vector3 pos, Vector3 dim){
		this.pos1 = pos;
		this.pos2 = pos.add( dim);
		fixPos();
	}
	
	void setZone2(Vector3 center, Vector3 dim){
		
		this.pos1 = new Vector3(center.x-dim.x/2, center.y, center.z - dim.z/2);
		this.pos2 = pos1.add( dim );
		fixPos();
	}
	
	Vector3 getNextPos(int inx, int iny, int inz ){
		
		int _x = inx;
		int _y = iny;
		int _z = inz;
		
		for(int x = 0 + _x; x < pos2.y - pos1.y;x ++){
			for(int y = 0 + _y; y < pos2.z - pos1.z; y++){
				for(int z = 0 + _z; z < pos2.x - pos1.x; z++){
					Vector3 v = new Vector3( pos1.x + x, pos1.y + y, pos1.z + z);
					Block block = world.getBlockAt( v.x, v.y, v.z);
					//m_NPC.faceTo(block.getLocation());
					if( !block.isEmpty() && !block.isLiquid() && m_NPC.isInSight(block)){
						//Messaging.log("breacking loc " + v.toString());
						return v;
					}
				}
				_z = 0;
			}
			_y = 0;
		}
		_x = 0;
		
		return new Vector3(0,-100000,0);
	}
	
	private void fixPos(){
		Vector3 pos3 = Vector3.ZERO();
		Vector3 pos4 = Vector3.ZERO();
		
		if(pos1.x < pos2.x){
			pos3.x = pos1.x;
			pos4.x = pos2.x;
		}
		else{
			pos3.x = pos2.x;
			pos4.x = pos1.x;
		}
		
		if(pos1.y < pos2.y){
			pos3.y = pos1.y;
			pos4.y = pos2.y;
		}
		else{
			pos3.y = pos2.y;
			pos4.y = pos1.y;
		}
		if(pos1.z < pos2.z){
			pos3.z = pos1.z;
			pos4.z = pos2.z;
		}
		else{
			pos3.z = pos2.z;
			pos4.z = pos1.z;
		}
		pos1=pos3;
		pos2 = pos4;
		
	}
	
	@Override
	public void Update(){
		if(target == null){
			target = getNextPos( curPos.x, curPos.y, curPos.z );
			
			if( target == null || target.y < -1000){
				if(lastCheck()){
					this.finished = true;
				}
				curPos = Vector3.ZERO();
				return;
			}
			else{
				curPos = new Vector3(target.x - pos1.x, target.y - pos1.y, target.z -pos1.z + 1 );
			}
		}else if(world.getBlockAt(target.x, target.y, target.z).isEmpty()){
			interaction = null;
			target = null;
			return;
		}
		
		if(interaction == null && target != null ){
			if(target.y < -1000){
				target = null;
				return;
			}
			Block block = world.getBlockAt(target.x, target.y, target.z);
			interaction =   actions.newInteration(block, this.m_NPC.getBukkitEntity().getItemInHand());
		}
		
		if(interaction != null){
			//the block is unbreakable
			if(interaction.getDuration() < 0){
				target = null;
				interaction = null;
				return;
			}
			interaction.Update();
			if(interaction.done()){
				actions.breakBlock(target);
				target = null;
				interaction = null;
			}
		}
		//actions.breakBlock(v);
	}
	
	public void Update2(){
		if(target == null){
			if(!targets.isEmpty())
				target = targets.remove(0);
			else{
				this.finished = true;
				return;
			}
		}else if(world.getBlockAt(target.x, target.y, target.z).isEmpty()){
			interaction = null;
			target = null;
			return;
		}
		
		if(interaction == null && target != null ){
			if(target.y < -1000){
				target = null;
				return;
			}
			Block block = world.getBlockAt(target.x, target.y, target.z);
			interaction =  actions.newInteration(block, this.m_NPC.getBukkitEntity().getItemInHand());
		}
		
		if(interaction != null){
			//the block is unbreakable
			if(interaction.getDuration() < 0){
				target = null;
				interaction = null;
				return;
			}
			interaction.Update();
			if(interaction.done()){
				actions.breakBlock(target);
				target = null;
				interaction = null;
			}
		}
	}
	
	public boolean Done2(){return targets.isEmpty();}
	
	private boolean lastCheck(){
		Vector3 pos = Vector3.ZERO();
		Messaging.log("lastCheck ");
		do{
			//Messaging.log("lop");
			target = getNextPos( pos.x, pos.y, pos.z );
			Messaging.log(target.toString());
			if(target != null){
				if(target.y <= -1000 ){
					
					return true;
				}
				
				Block block = world.getBlockAt(target.x, target.y, target.z);
				interaction =   actions.newInteration(block, this.m_NPC.getBukkitEntity().getItemInHand());
				pos = new Vector3(target.x - pos1.x, target.y - pos1.y, target.z -pos1.z + 1 );
			}
			if(interaction != null){
				if(interaction.getDuration() > 0 && interaction.getDuration() < 50 ) {
					return false;
				}
			}
		}while(target == null);
		return true;
	}
	
	public void addTarget(Vector3 pos){
		targets.add(pos);
	}
	
	public String toString(){
		return "CleanTask";
	}
}
