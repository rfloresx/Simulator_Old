package com.github.otrebor4.simulator.util;

import org.bukkit.Location;

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
	
	public void copyValuesFrom( Vector3 other){
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	public static Vector3 fromLocation(Location loc){
		return new Vector3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
}
