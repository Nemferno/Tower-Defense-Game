package org.xodia.td.immunity;

import java.util.ArrayList;

import org.xodia.td.entity.enemy.EnemyType;

// Manages the Immunity And ETC
public class ImmunityManager {

	private static ImmunityManager instance;
	
	private Thread immunityManagerThread;
	
	private ArrayList<EnemyType> enemyTypes;
	
	private boolean isRunning;
	
	private Runnable run = new Runnable(){
		public void run(){
			while(isRunning){
				for(int i = 0; i < enemyTypes.size(); i++){
					if(!isRunning)
						break;
					
					EnemyType type = enemyTypes.get(i);
					
					int averageTimeKill = type.getAverageTimeKill();
					int averageSlowFreeze = type.getAverageSlowFreezeTimes();
					int averageKills = type.getAverageKills();
					
					// TODO Implement List Size Ifs
					// I.E
					// if(averageTimeKill > 0 && averageTimKill <= 1750 && averageTimeKillSize > 10) then implement immunity
					
					////////////////////////////// DEFENSE MATRIX ///////////////////////////////////
					if(averageTimeKill > 0 && averageTimeKill <= 3500){
						// Implement it
						float percentage = (averageTimeKill / 3500);
						float defensivePercentage = ((10 * percentage) + 25) / 100;
						
						type.setImmunityWith(new DefenseMatrix(defensivePercentage));
					}else{
						// Remove it
						type.removeImmunity(new DefenseMatrix(0));
					}
					////////////////////////////// DEFENSE MATRIX ///////////////////////////////////
					
					////////////////////////////// INSULATOR ///////////////////////////////
					if(averageSlowFreeze > 8 && averageSlowFreeze > 15){
						type.setImmunityWith(new Insulator());
					}else{
						type.removeImmunity(new Insulator());
					}
					////////////////////////////// INSULATOR ///////////////////////////////
					
					////////////////////////////// STIM PACK /////////////////////////////
					if(averageTimeKill > 0 && averageTimeKill <= 2500 && averageKills > 0 && averageKills <= 5){
						float averageTimeKillPercentage = (averageTimeKill / 2500);
						averageTimeKillPercentage = (averageTimeKillPercentage * 7) / 100;
						
						float averageKillsPercentage = (averageKills / 5);
						averageKillsPercentage = (averageKillsPercentage * 13) / 100;
						  
						float totalPercentage = averageTimeKillPercentage + averageKillsPercentage + 0.20f;
						
						float healthLossPercentage = (((totalPercentage / 0.40f) * 20) + 5) / 100;
						
						type.addImmunity(new StimPack(totalPercentage, healthLossPercentage));
					}else{
						type.removeImmunity(new StimPack(0, 0));
					}
					////////////////////////////// STIM PACK /////////////////////////////
					
					/////////////////////////////////// IMMORTALITY ///////////////////////////////
					// WHEN DO WE USE IT?
					// WHEN ITS KILLS IS LESS THAN 2 AND ITS KILL TIME IS LESS THAN 1500
					if(averageKills <= 2 && averageTimeKill <= 1500){
						// HOW DO WE DETERMINE THE AMOUNT OF ARMOR...
						// HOW MUCH DO WE NEED? 5-15% == Armor 1 - 3
						int armor = (int) (averageTimeKill / 3);
						
						type.addImmunity(new Immortality(armor));
					}else{
						type.removeImmunity(new Immortality(0));
					}
					/////////////////////////////////// IMMORTALITY ///////////////////////////////
				}
			}
		}
	};
	
	private ImmunityManager(){
		enemyTypes = new ArrayList<EnemyType>();
		enemyTypes.add(EnemyType.DARKROVER);
		enemyTypes.add(EnemyType.IMMORTAL);
		enemyTypes.add(EnemyType.TROJAN);
		enemyTypes.add(EnemyType.VIRUS);
		enemyTypes.add(EnemyType.WORM);
		
		// The future problem that is involved with threading is 
		// the concurrency. There will be data corruption and out
		// of sync data. So, we have to make it so that it has to
		// refresh if new data if available
		immunityManagerThread = new Thread(run);
	}
	
	public static ImmunityManager getInstance(){
		if(instance == null)
			instance = new ImmunityManager();
		
		return instance;
	}
	
	public void start(){
		isRunning = true;
		
		// check if another thread is running if so, stop it
		// we have to make this so that it no longer 
		immunityManagerThread = new Thread(run);
	
		if(immunityManagerThread != null)
			immunityManagerThread.start();
		else
			System.out.println("ERROR: CANNOT INIT CLIENT DUE TO NULL");
	}
	
	public void stop(){
		isRunning = false;
	}
	
	public void reset(){
		for(EnemyType e : enemyTypes){
			e.clearImmunities();
			e.clearData();
		}
	}
	
}
