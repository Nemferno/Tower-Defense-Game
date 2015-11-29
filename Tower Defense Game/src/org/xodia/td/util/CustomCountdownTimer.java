package org.xodia.td.util;

public class CustomCountdownTimer {

	private int goalTime;
	private int currentTime;
	
	private boolean canStart;
	private boolean isTimeElapsed;
	
	public CustomCountdownTimer(int goalTime){
		this.goalTime = goalTime;
	}
	
	public void tick(int delta){
		if(canStart){
			if(!isTimeElapsed){
				currentTime += delta;
				
				if(currentTime > goalTime){
					isTimeElapsed = true;
				}
			}
		}
	}
	
	public void setTime(int goalTime){
		this.goalTime = goalTime;
		
		reset();
	}
	
	public void start(){
		canStart = true;
	}
	
	public void reset(){
		isTimeElapsed = false;
		currentTime = 0;
	}
	
	public int getGoalTime(){
		return goalTime;
	}
	
	public int getTimeLeftInSeconds(){
		return (goalTime - currentTime) / 1000;
	}
	
	public int getTimeLeftInMilliSeconds(){
		return goalTime - currentTime;
	}
	
	public int getTimeElapsedInSeconds(){
		return currentTime / 1000;
	}
	
	public int getTimeElapsedInMilliSeconds(){
		return currentTime;
	}
	
	public boolean isTimeElapsed(){
		return isTimeElapsed;
	}
	
	public boolean canStart(){
		return canStart;
	}
	
}
