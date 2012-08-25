package com.github.otrebor4.simulator.util;

import java.util.HashMap;


public class Construction {
	HashMap<Vector3, BlocksData > blocks = new HashMap<Vector3, BlocksData>();
	private Vector3 Dimension = null;
	private Vector3 WorldPos = Vector3.ZERO();
	
	public void setDimension(Vector3 dim){
		Dimension = dim;
	}
	
	public Vector3 getDimension(){
		if(Dimension == null){
			Dimension = findDimension();
		}
		return Dimension;
	}
	
	private Vector3 findDimension(){
		int x = 0, y = 0, z = 0;
		for(Vector3 pos : blocks.keySet()){
			if(x < pos.x)
				x = pos.x;
			if( y < pos.y)
				y = pos.y;
			if( z < pos.z)
				z = pos.z;
		}
		return new Vector3(x, y, z);
	}
	
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
	
	public Vector3 getCenterXZ(){
		int x = 0, z = 0;
		Vector3 dim = getDimension();
		if(dim != null){
			x = dim.x/2;
			z = dim.y/2;
		}
		return getWorldPos( new Vector3(x, 0, z));
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
