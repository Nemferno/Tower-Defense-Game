package org.xodia.td.entity.enemy.boss;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.Path;
import org.xodia.td.attribute.BossAttribute;
import org.xodia.td.entity.GameObject;
import org.xodia.td.entity.SubSecuritySystem;
import org.xodia.td.entity.enemy.BasicEnemy;
import org.xodia.td.entity.turret.BasicTurret;
import org.xodia.td.map.BasicMap;
import org.xodia.td.map.TempMap;
import org.xodia.td.util.CustomCountdownTimer;

public class BasicBoss extends BasicEnemy{

	protected static final int 	LARGE = 3,
								NORMAL = 2,
								SMALL = 1,
								TINY = 0;
	
	protected CustomCountdownTimer delayAttack;
	
	protected int currentSize;
	
	protected boolean isProvoked;
	protected boolean hasImplementedProvoked;
	
	protected boolean hasAttackedOnce;
	protected boolean minimize;
	protected boolean canMinimize = true;
	
	// What the type contains
	protected ArrayList<BossAttribute> attributeList;
	
	public BasicBoss(BasicMap origMap, ArrayList<Path> pathList, TempMap turretMap, float x,
			float y, GameObject initTarget, boolean isAggressor) {
		super(origMap, pathList, turretMap, x, y, initTarget, isAggressor);
		
		attributeList = new ArrayList<BossAttribute>();
		
		delayAttack = new CustomCountdownTimer(500);
		delayAttack.start();
		
		currentSize = NORMAL;
	}
	
	public void update(int delta) {
		super.update(delta);
		
		
		if(minimize){
			if(currentSize < TINY){
				setAlive(false);
				canMinimize = false;
			}else{
				setCurrentHealth(getMaxHealth());
				setCurrentShield(getMaxShield());
				setAlive(true);
				
				currentSize--;
				minimize = false;
			}
		}
		
		if(isProvoked && !hasImplementedProvoked){
			setAttackSpeed(getAttackSpeed() - (getAttackSpeed() * 0.5f));
			incMaxHealth(200);
			incCurrentHealth(200);
			incMaxShield(150);
			incCurrentShield(150);
			incDamage(15);
			
			hasImplementedProvoked = true;
		}
	}

	protected void attack(int delta) {
		if(rateOfFire.isTimeElapsed()){
			if(getTarget() != null){
				if(getTarget() instanceof SubSecuritySystem){
					if(((SubSecuritySystem) getTarget()).getTarget() == null)
						((SubSecuritySystem) getTarget()).setTarget(this);
				}
				
				if(getTarget() instanceof BasicTurret){
					((BasicTurret) getTarget()).setEnemyAttackingTurret(this);
				}
				
				if(attribute == BossAttribute.DOUBLEATTACK){
					// Do not reset!
					if(hasAttackedOnce){
						if(delayAttack.isTimeElapsed()){
							// Repeat
							float difference = getTarget().getCurrentShield() - getDamage();
							float leftOver = 0;
							
							if(difference < 0){
								leftOver = (difference * -1) - (getTarget().getArmor() * 2);
								
								if(leftOver <= 0)
									leftOver = 0.1f;
								
								getTarget().setCurrentShield(0);
								getTarget().incCurrentHealth(-leftOver);
							}else{
								getTarget().setCurrentShield(difference);
							}
							
							rateOfFire.reset();
							delayAttack.reset();
						}else{
							delayAttack.tick(delta);
						}
					}else{
						float difference = getTarget().getCurrentShield() - getDamage();
						float leftOver = 0;
						
						if(difference < 0){
							leftOver = (difference * -1) - (getTarget().getArmor() * 2);
							
							if(leftOver <= 0)
								leftOver = 0.1f;
							
							getTarget().setCurrentShield(0);
							getTarget().incCurrentHealth(-leftOver);
						}else{
							getTarget().setCurrentShield(difference);
						}
						
						hasAttackedOnce = true;
					}
				}else{
					float difference = getTarget().getCurrentShield() - getDamage();
					float leftOver = 0;
					
					if(difference < 0){
						leftOver = (difference * -1) - (getTarget().getArmor() * 2);
						
						if(leftOver <= 0)
							leftOver = 0.1f;
						
						getTarget().setCurrentShield(0);
						getTarget().incCurrentHealth(-leftOver);
					}else{
						getTarget().setCurrentShield(difference);
					}
					
					rateOfFire.reset();
				}
			}
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
		
		g.setColor(Color.red);
		g.draw(getBounds());
	}
	
	public void setMinimized(boolean minimized){
		minimize = minimized;
	}
	
	public void setProvoked(boolean provoke){
		isProvoked = provoke;
	}
	
	public void setSize(int size){
		this.currentSize = size;
	}
	
	public void addBossAttribute(BossAttribute attribute){
		attributeList.add(attribute);
	}
	
	public void removeBossAttribute(BossAttribute attribute){
		attributeList.remove(attribute);
	}
	
	public boolean isMinimized(){
		return minimize;
	}
	
	public boolean isProvoked(){
		return isProvoked;
	}
	
	public int getCurrentSize(){
		return currentSize;
	}
	
	public boolean canMinimize(){
		return canMinimize;
	}
	
	public ArrayList<BossAttribute> getBossAttributeList(){
		return attributeList;
	}

}
