package com.github.otrebor4.simulator.util;

public class Timer {
	long startTime;
	
	public Timer(){
		startTime = System.currentTimeMillis();
	}
	
	public void start(){
		startTime = System.currentTimeMillis();
	}
	
	public double getTimeSecons(){
		long curTime = System.currentTimeMillis();
		return (curTime - startTime)/1000;
	}
	
}
