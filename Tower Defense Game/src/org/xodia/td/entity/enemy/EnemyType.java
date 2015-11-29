package org.xodia.td.entity.enemy;

import java.util.ArrayList;

import org.xodia.td.immunity.Immunity;

public enum EnemyType {

	VIRUS, BROODLING, DARKROVER, IMMORTAL, WORM, CONTROLLEDTURRET, TROJAN;
	
	// TODO Change Mechanics of Immunity Better
	private ArrayList<Immunity> immunityList = new ArrayList<Immunity>();
	
	private EnemyData data = new EnemyData();
	
	public void incrementDeath(){
		data.addDeath(1);
	}
	
	public void incrementKill(int kills){
		data.addKill(kills);
	}
	
	public void addTimeKill(int timekill){
		data.addTimeKill(timekill);
	}
	
	public void addSlowFreezeTimes(int times){
		data.addSlowFreezeNums(times);
	}
	
	public void clearData(){
		data.clearData();
	}
	
	public synchronized void addImmunity(Immunity immunity){
		// Check if there is another immunity
		for(Immunity i : immunityList){
			if(i.getClass().getSimpleName().equals(immunity.getClass().getSimpleName())){
				return;
			}
		}
		
		immunityList.add(immunity);
	}
	
	// This is only used to replace the same immunity with the new one
	public synchronized void setImmunityWith(Immunity immunity){
		int index = 0;
		boolean isFound = false;
		
		for(Immunity i : immunityList){
			if(i.getClass().getSimpleName().equals(immunity.getClass().getSimpleName())){
				isFound = true;
				break;
			}
			
			index++;
		}
		
		if(!isFound){
			addImmunity(immunity);
		}else{
			immunityList.set(index, immunity);
		}
	}
	
	public synchronized void removeImmunity(Immunity immunity){
		if(immunityList.size() != 0){
			int index = 0;
			boolean isFound = false;
			
			for(Immunity i : immunityList){
				if(i.getClass().getSimpleName().equals(immunity.getClass().getSimpleName())){
					isFound = true;
					break;
				}
				
				index++;
			}
			
			if(isFound)
				immunityList.remove(index);
		}
	}
	
	public synchronized void clearImmunities(){
		immunityList = new ArrayList<Immunity>();
	}
	
	public synchronized Immunity pollImmunity(int index){
		return immunityList.get(index);
	}
	
	public synchronized  int getImmunityCount(){
		return immunityList.size();
	}
	
	public synchronized  int getAverageTimeKill(){
		return data.getAverageTimeKill();
	}
	
	public synchronized int getAverageSlowFreezeTimes(){
		return data.getAverageSlowFreezeNumbers();
	}
	
	public synchronized int getAverageKills(){
		return data.getAverageKills();
	}
	
	public synchronized int getDeaths(){
		return data.getNumOfDeaths();
	}
	
}
