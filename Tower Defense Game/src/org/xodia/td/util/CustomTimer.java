package org.xodia.td.util;

// Unlike the CustomCountdownTimer, this timer goes up without
// ever stopping unless it is told to stop. Basically a StopWatch
public class CustomTimer {

	private int currentTime;
	
	private boolean isTiming;
	
	public void tick(int delta){
		if(isTiming){
			currentTime += delta;
		}
	}
	
	public void start(){
		isTiming = true;
	}
	
	public void stop(){
		isTiming = false;
	}
	
	public void reset(){
		currentTime = 0;
	}
	
	public double getTimeInSeconds(){
		return currentTime / 1000;
	}
	
	public int getTimeInMilliSeconds(){
		return currentTime;
	}
	
}
