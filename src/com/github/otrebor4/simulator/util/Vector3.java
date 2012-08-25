package com.github.otrebor4.simulator.util;

public class Vector3 {
	public int x, y, z;
	
	public Vector3( int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(int val){
		y=z=x=val;
		
	}
	
	public static Vector3 ZERO(){
		return new Vector3(0,0,0);
	}
	
	public Vector3 add( Vector3 other ){
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector3 Sub( Vector3 other){
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}
	
	public String toString(){
		return "(" + x + "," +y + "," + z +")";
	}
	
	
		
	
}
