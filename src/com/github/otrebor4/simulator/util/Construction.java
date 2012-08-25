package com.github.otrebor4.simulator.util;

import java.util.HashMap;


public class Construction {
	HashMap<Vector3, BlocksData > blocks = new HashMap<Vector3, BlocksData>();
	private Vector3 WorldPos = Vector3.ZERO();
	
	public void addBlock(Vector3 pos, int blockid){
		blocks.put( pos, new BlocksData( blockid, pos, 0));
	}
	
	public void SetWordPos(Vector3 pos ){
		WorldPos = pos;
	}
	
	public Vector3 getNextPos(){
		for(Vector3 pos: blocks.keySet()){
			return pos;
		}
		return null;
	}
	
	public int getBlockIdPos(Vector3 pos ){
		if(blocks.containsKey( pos )){
			return blocks.get(pos).blockid;
		}
		return -1;
	}
	
	public void remobeBlockPos(Vector3 pos){
		if(blocks.containsKey( pos)){
			blocks.remove(pos);
		}
	}
	
	public Vector3 getWorldPos(Vector3 pos){
		return pos.add( WorldPos);
	}
	
	class BlocksData{
		int priority;
		Vector3 pos;
		int blockid;
		BlocksData(int blockid, Vector3 pos, int val){
			this.blockid = blockid;
			this.pos = pos;
			this.priority = val;
		}
	}
}
