package com.github.otrebor4.simulator.SP.tasks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.inventory.PlayerInventory;
import com.github.otrebor4.simulator.SP.actinions.Action;
import com.github.otrebor4.simulator.SP.actinions.CollectItem;
import com.github.otrebor4.simulator.resources.CraftSP;
import com.github.otrebor4.simulator.util.Messaging;
import com.github.otrebor4.simulator.util.Vector3;
import com.github.otrebor4.simulator.util.WorldUtils;

public class CollectTask extends Task{
	HashMap<Integer, Integer> material = null;
	CleanTask clean;
	
	public CollectTask(CraftSP npc) {
		super(npc);
		action = getActions(npc);
		clean = new CleanTask(npc );
	}
	
	public CollectTask(CraftSP npc,int count, int ... ids){
		super(npc);
		List<Action> actions = new LinkedList<Action>();
		material = new HashMap<Integer, Integer> ();
		for(int id : ids){
			actions.add( new CollectItem(npc, id, 10.0f));
			material.put(id, count);
		}
		this.action = actions;
		clean = new CleanTask(npc );
	}
	
	public CollectTask(CraftSP npc, HashMap<Integer, Integer> list ){
		super(npc);
		material = list;
	}
	
	@Override
	public void Update(){
		if(material == null){
			super.Update();
			return;
		}
		Messaging.log("updating collectTask");
		Update1();
		UpdateMaterials();
		if(material.isEmpty()){
			this.finished = true;
			return;
		}
		if( (action == null || action.isEmpty()) && CollectItem.getClosestItemOfIds(m_NPC, 10 , material.keySet()) == null){
			FindMaterial();
		}
		else if(action == null || action.isEmpty()){
			if(material != null && !material.isEmpty()){
				for( int key : material.keySet()){
					action = getActions( this.m_NPC, key);
					Messaging.log("action " + action.toString());
					return;
				}
			}
		}
	}
	
	private void Update1(){
		if(action != null && !action.isEmpty() && action.get(0) == null){
			action.remove(0);
		}
		
		if(action == null || action.isEmpty()){
			return;
		}
		
		action.get(0).Update();
		
		if(action.get(0).done()){
			action.remove(0);
		}
	}
	
	private void UpdateMaterials(){
		PlayerInventory inv = m_NPC.getBukkitEntity().getInventory();
		List<Integer> rkey= new LinkedList<Integer>();
		for( Integer key : material.keySet()){
			int id = BlocksItemsList.instance.getItemId(key);
			if(inv.contains( id, material.get(key))){
				rkey.add(key);
			}
		}
		for(Integer key : rkey){
			if(material.containsKey(key)){
				material.remove(key);
			}
		}
	}
	
	private void FindMaterial(){
		if(clean == null){
			clean = new CleanTask(this.m_NPC);
			return;
		}
		if(!clean.Done2()){
			clean.Update2();
		}
		else{
			for(int id : material.keySet()){
				Block target = getCloseBlockPosOfIds( getBlockidsforItem(id ), 20 );
				if(target != null){
					clean.addTarget( Vector3.fromLocation(target.getLocation()));
					return;
				}
			}
		}
	}
	
	public List<Action> getActions( CraftSP npc){
		List<Action> actions = new LinkedList<Action>();
		actions.add( new CollectItem(npc,10));
		return actions;
	}
	
	public List<Action> getActions( CraftSP npc, int id){
		List<Action> actions = new LinkedList<Action>();
		//actions.add( new CollectItem(npc, id, 10.0f));
		actions.add( new CollectItem(npc,10));
		return actions;
	}
	
	public Block getCloseBlockPosOfIds(int [] ids, int range){
		Block block = null;
		double dis = 1000000;
		for(int id : ids){
			Block b = getCloseBlockOfId(id, range);
			if(b != null){
				double temp =  b.getLocation().distance( m_NPC.getBukkitEntity().getLocation());
				if(temp < dis){
					block = b;
					dis = temp;
				}
			}
		}
		return block;
	}
	
	public Block getCloseBlockOfId( int id, int range){
		List<Block> blocks = WorldUtils.getBlocksOfId(m_NPC.getBukkitEntity().getLocation(), id, range);
		Block temp = null;
		double dist = 100000000;
		for(Block block : blocks){
			if(m_NPC.isInSight(block)){
				double newdist = m_NPC.getBukkitEntity().getLocation().distance( block.getLocation()) ;
				if( newdist < dist ){
					temp = block;
					dist = newdist;
				}
			}
		}
		return temp;
	}
	
	public int [] getBlockidsforItem( int id){
		return BlocksItemsList.instance.getBlockids(id);
	}
	
	//get block id, of item
	private static class BlocksItemsList{
		HashMap<Integer, int [] > data = new HashMap<Integer, int []>();
		
		public BlocksItemsList(){
			data.put( 4, new int [] {1,4});
			data.put(324,  new int []{64});
			data.put(330,  new int []{71 });
			data.put(338,  new int []{83 });
			data.put(354,  new int []{92 });
			data.put(356,  new int []{93,94});
			data.put(379,  new int []{117 });
			data.put(380,  new int []{118});
			data.put(284,  new int []{132 });
			data.put(263,  new int []{16});
			data.put(265,  new int []{15});
			//data.put(351,  new int []{21});
			data.put(266,  new int []{14});
			data.put(331,  new int []{73, 74});
			data.put(268,  new int []{56});
			data.put(388,  new int []{129});
	
		}
		
		public int getItemId(int Blockid){
			for(int id : data.keySet()){
				int [] A = data.get(id);
				for(int id2: A){
					if(id2 == Blockid){
						return id;
					}
				}
			}
			return Blockid;
		}
		
		public int [] getBlockids(int id){
			if(data.containsKey(id)){
				return data.get(id);
			}
			return new int [] {id};
		}
		
		public static BlocksItemsList instance = new BlocksItemsList();
		
		
	}

	
	
}
