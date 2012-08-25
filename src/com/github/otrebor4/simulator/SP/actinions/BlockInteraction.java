package com.github.otrebor4.simulator.SP.actinions;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.otrebor4.simulator.resources.Animator.Animation;
import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.util.Tools;

public class BlockInteraction{
	
	int targetId;
	float duration;
	ItemStack tool;
	boolean finish = false;
	Location targ;
	CraftSP npc;
	static float MIN_DIST = 5f;
	Action action;
	long starTime = 0;
	boolean started = false;
	
	public BlockInteraction(Block block, ItemStack item, CraftSP npc){
		//net.minecraft.server.Block.byId[block.getTypeId()].a( npc );
		targ = block.getLocation();
		duration = Tools.getDurability( block.getTypeId(), item.getTypeId());
		//starTime = System.currentTimeMillis();
		tool = item;
		this.npc = npc;
		targetId = block.getTypeId();
		//Messaging.log("duration is " + duration);
		
		
	}
	
	public void setStartTime( long time ){
		starTime = time;
	}
	
	public void Update(){
		double dis = targ.distance(npc.getBukkitEntity().getLocation());
		//Messaging.log("distance is " + dis);
		if( dis < MIN_DIST ){
			if( npc.IsFacingT( targ )){
				if(!started){
					setStartTime(System.currentTimeMillis() );
					started = true;
				}
				npc.performAction(Animation.SWING_ARM);
				//duration -= Tools.getDameByTool(targetId, tool.getTypeId());
				long dif =  System.currentTimeMillis() - starTime;
				
				//Messaging.log( "duration " + duration + " dif " + (float)dif/1000.0f + "   " + System.currentTimeMillis()/1000);
				if(duration <= (float)dif/1000.0f ){
					finish = true;
				}
			}else{
				npc.faceTo( targ);
			}
		}
		else{
			npc.MoveToPos( targ.getBlockX(), targ.getBlockY(), targ.getBlockZ());
		}
	
	}
	
	public boolean done(){
		return finish;
	}

	public float getDuration(){
		return duration;
	}


}
