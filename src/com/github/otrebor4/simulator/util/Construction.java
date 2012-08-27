package com.github.otrebor4.simulator.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Construction {
	HashMap<Vector3, BlocksData > blocks = new HashMap<Vector3, BlocksData>();
	List<Vector3> keyset = new LinkedList<Vector3>();
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
		keyset.add( pos );
	}
	
	public void SetWordPos(Vector3 pos ){
		WorldPos = pos;
	}
	
	public Vector3 getNextPosIg(int... blockids){
		for(Vector3 pos: keyset){
			boolean ig = false;
			for(int id : blockids){
				if( blocks.get(pos).blockid == id ){
					ig = true;
				}
			}
			if(!ig){
				return pos;
			}
		}
		return null;
		
	}
	
	public Vector3 getNextPos(){
		if( !keyset.isEmpty()){
			return keyset.get(0);
		}
		return null;
	}
	
	public int getBlockIdPos(Vector3 pos ){
		if(blocks.containsKey( pos )){
			return blocks.get(pos).blockid;
		}
		return -1;
	}
	
	public BlocksData remobeBlockPos(Vector3 pos){
		if(pos == null)
			return null;
		if(blocks.containsKey( pos)){
			keyset.remove(pos);
			return blocks.remove(pos);
		}
		return null;
	}
	
	public BlocksData getBlockPos(Vector3 pos){
		if(pos != null){
			if(blocks.containsKey( pos )){
				return blocks.get(pos);
			}
		}
		return null;
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
		return getWorldPos( new Vector3(x, 1, z));
	}
	
	public static class BlocksData{
		public int priority;
		public Vector3 pos;
		public int blockid;
		BlocksData(int blockid, Vector3 pos, int val){
			this.blockid = blockid;
			this.pos = pos;
			this.priority = val;
		}
		@Override
		public String toString(){
			return "Pos " + pos.toString() + " id " + blockid;
			
		}
	}
}
