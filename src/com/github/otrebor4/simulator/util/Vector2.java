package com.github.otrebor4.simulator.util;

public class Vector2 {
	public int x;
	public int y;
	
	public Vector2(int x, int y){
		this.x = x; 
		this.y = y;
	}
	
	public static Vector2 getRVRange(int min, int max){
		int x ;
		int y;
		do{
			x = Rand.random.nextInt(max - min + 1) + min;
		}while( x < min || x > max);
		do{
			y= Rand.random.nextInt(max - min + 1) + min;
		}while( y < min || y > max);
	
		return new Vector2(x, y);
	}
}
