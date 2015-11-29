package org.xodia.td.entity.enemy;

import java.util.ArrayList;

public class EnemyData {

	// The Numbers Of Deaths In Total For The Enemy Of The Same Type
	private int numOfDeaths;
	
	// The Numbers Of Kills In Total For The Enemy Of The Same Type
	private ArrayList<Integer> killList;
	
	// All The Time Kills Of An Enemy
	private ArrayList<Integer> timeKills;
	
	// The Number Of Slowness & Freeze Occurred Of The Enemy
	private ArrayList<Integer> slowFreezeNums;
	
	public EnemyData(){
		timeKills = new ArrayList<Integer>();
		slowFreezeNums = new ArrayList<Integer>();
		killList = new ArrayList<Integer>();
	}
	
	public synchronized void addDeath(int amount){
		numOfDeaths += amount;
	}
	
	public synchronized void addKill(int amount){
		killList.add(amount);
	}
	
	public synchronized void addTimeKill(int time){
		timeKills.add(time);
	}
	
	public synchronized void addSlowFreezeNums(int times){
		slowFreezeNums.add(times);
	}
	
	public synchronized void clearData(){
		numOfDeaths = 0;
		
		killList = new ArrayList<Integer>();
		timeKills = new ArrayList<Integer>();
		slowFreezeNums = new ArrayList<Integer>();
	}
	
	public synchronized int getNumOfDeaths(){
		return numOfDeaths;
	}
	
	public synchronized int getAverageKills(){
		int totalKills = 0;
		
		for(Integer i : killList){
			totalKills += i;
		}
		
		if(totalKills != 0 && timeKills.size() != 0)
			totalKills /= timeKills.size();
		
		return totalKills;
	}
	
	// The problem is that the object is not there!
	public synchronized int getAverageTimeKill(){
		int totalTimeKill = 0;
		
		for(Integer i : timeKills){
			totalTimeKill += i;
		}
		
		if(totalTimeKill != 0)	
			totalTimeKill /= timeKills.size();
		
		return totalTimeKill;
	}
	
	public synchronized int getAverageSlowFreezeNumbers(){
		int total = 0;
		
		for(int i : slowFreezeNums)
			total += i;
		
		if(total != 0)
			total /= slowFreezeNums.size();
		
		return total;
	}
	
}
